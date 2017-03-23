package HtmlParserApp;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) throws IOException, UnsupportedEncodingException {
//		PrintWriter indexFile = new PrintWriter("indexFile.txt", "UTF-8");
//		Helper helper = new Helper();
//		helper.SetStopWordsList();
//		
//		List<File> htmlFiles = new ArrayList<File>();
//		htmlFiles = helper.GetAllFilesFromDirectory("/D:/workspace/RIW/WorkingDirectory", ".htm", ".html");
//		
//		for(File item : htmlFiles){
//			System.out.print(item.getName());
//			System.out.println(" Path:"+ item.getPath());
//			
//			File input = new File(item.getPath());
//			try{
//				helper.PopulateDictionary(input);
//				helper.WriteIndexer(item);
//				indexFile.println(item.getPath());
//				indexFile.println(item.getName());
//				indexFile.println();
//			}catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		indexFile.close();
//		
//		List<File> indexFiles = new ArrayList<File>();
//		indexFiles = helper.GetAllFilesFromDirectory("/D:/workspace/RIW/Lab01", ".htm.json", ".html.json");
//		for(File item : indexFiles){
//			System.out.println(item.getName());
//		}
//		
//		helper.InvertedIndex(indexFiles);
//		
//		SearchHelper sh = new SearchHelper();
//		sh.BooleanSearch(helper);
//		
//		for(int i=0;i<sh.resultList.size(); i++){
//			System.out.println(sh.resultList.get(i).file);
//		}		
		Porter p = new Porter();
		WordMap wm = new WordMap();
		p.InitializeVowels();
		wm = p.GetWordMapping("motoring");
		System.out.println(wm.word + "  " + wm.m);
		
		String s = p.FirstStepB("motoring", wm);
		System.out.println(s);
	}
}
