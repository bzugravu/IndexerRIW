package HtmlParserApp;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
	
	public static BlockingQueue<String> fileNameQueue = null;

	public static void main(String[] args) throws IOException, UnsupportedEncodingException {
		PrintWriter indexFile = new PrintWriter("indexFile.txt", "UTF-8");
		Porter porter = new Porter();
		Helper helper = new Helper(porter);
		ExecutorService executor = Executors.newFixedThreadPool(5);
		helper.SetStopWordsList();
		
		List<File> htmlFiles = new ArrayList<File>();
		htmlFiles = helper.GetAllFilesFromDirectory("/D:/workspace/RIW/WorkingDirectory", ".htm", ".html");
		
		for(File item : htmlFiles){
			System.out.println(item.getName());
//			System.out.println(" Path:"+ item.getPath());
			try{
//				File input = new File(item.getPath());			
//				helper.PopulateDictionary(input);
//				helper.WriteIndexer(item);
				
				Runnable worker = new MapThread(item, helper, porter);
				executor.execute(worker);
				indexFile.println(item.getPath());
				indexFile.println(item.getName());
				indexFile.println();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		executor.shutdown();
        while (!executor.isTerminated()) {
        	
        }
        
        
		indexFile.close();
		
		List<File> indexFiles = new ArrayList<File>();
		indexFiles = helper.GetAllFilesFromDirectory("/D:/workspace/RIW/IndexerRIW", ".htm.json", ".html.json");
		for(File item : indexFiles){
			System.out.println(item.getName());
		}
		
		helper.InvertedIndex(indexFiles);
		
		SearchHelper sh = new SearchHelper(porter);
//		sh.BooleanSearch(helper);
//		
//		System.out.println("***********************************************");
//		for(int i=0;i<sh.resultList.size(); i++){
//			System.out.println(sh.resultList.get(i).file);
//		}			
//		System.out.println("***********************************************");
		
		Frequency resultFrequency = new Frequency(sh, helper);
		
		if(resultFrequency.searchQuery.length > 1){
		resultFrequency.DocumentFrequency();
		resultFrequency.TermFrequency();
		resultFrequency.SetQueryParams();
		resultFrequency.SetDistances();
		
		for(Map.Entry<String, Double> entry : resultFrequency.distance.entrySet()){
			System.out.println(entry.getKey() + " -- " + entry.getValue().toString());
		}
		}
		else{
			for(int i=0;i<sh.resultList.size(); i++){
				System.out.println(resultFrequency.fileList.get(i).file);
			}	
		}
	}
}
