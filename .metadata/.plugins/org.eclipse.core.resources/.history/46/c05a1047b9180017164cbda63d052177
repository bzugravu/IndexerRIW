package HtmlParserApp;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
	
	public static BlockingQueue<String> fileNameQueue = null;

	public static void main(String[] args) throws IOException, UnsupportedEncodingException {
		PrintWriter indexFile = new PrintWriter("indexFile.txt", "UTF-8");
		Helper helper = new Helper();
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
				
				Runnable worker = new MapThread(item, helper);
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
		
		SearchHelper sh = new SearchHelper();
		sh.BooleanSearch(helper);
		
		for(int i=0;i<sh.resultList.size(); i++){
			System.out.println(sh.resultList.get(i).file);
		}	
		
		
	}
}
