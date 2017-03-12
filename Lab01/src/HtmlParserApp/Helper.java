package HtmlParserApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Helper {
	
	public List<String> stopWords = new ArrayList<>();
	
	public List<String> exceptionWords = new ArrayList<>();
	
	public Map<String, Integer> myDictionary = new HashMap<String, Integer>();
	
	public Map<String, String> myInvertedIndexMap = new HashMap<String, String>();
	
	public void SetStopWordsList(){
		try {
			File file = new File("stopWords.txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stopWords.add(line);
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void SetExceptionWordsList(){
		try {
			File file = new File("stopWords.txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				exceptionWords.add(line);
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void ParseDocument(File input) throws IOException{
		HtmlParser htmlParser = new HtmlParser();
		Document doc = htmlParser.ReadHtmlFile(input);
		String title = htmlParser.GetTitle(doc);
		String text = htmlParser.GetText(doc);
		List<String> keywordsAttContent = htmlParser.GetAttributeContent(doc, "meta", "keywords");
		List<String> descriptionAttContent = htmlParser.GetAttributeContent(doc, "meta", "description");
		List<String> robotsAttContent = htmlParser.GetAttributeContent(doc, "meta", "robots");
		List<String> anchorLinks = htmlParser.GetAnchors(doc);
		
		System.out.println("Title:" + title);
		System.out.println("Keywords:" + keywordsAttContent.toString());
		System.out.println("Description:" + descriptionAttContent);
		System.out.println("Robots:" + robotsAttContent);
		System.out.println("Links:" + anchorLinks);
		System.out.println("Text:" + text);
	}
	
	public void PopulateDictionary(File input) throws IOException{
		myDictionary.clear();
		HtmlParser htmlParser = new HtmlParser();
		Document doc = htmlParser.ReadHtmlFile(input);
		String text = htmlParser.GetText(doc);
		StringBuilder word = new StringBuilder();
		for(char c : text.toCharArray()){
			if(Character.isLetter(c)){
				word.append(c);
			}
			else{
				if(!word.toString().isEmpty()){
					/*if(exceptionWords.contains(word))
					//TODO
					 */
					//word = word.toLowerCase();
					if(stopWords.contains(word.toString().toLowerCase()))
						continue;					
					if(myDictionary.containsKey(word.toString().toLowerCase()))
						myDictionary.put(word.toString().toLowerCase(), myDictionary.get(word.toString().toLowerCase()) + 1);
					else
						myDictionary.put(word.toString().toLowerCase(), 1);
				}
				
				word.replace(0, word.length(), "");
			}
		}
	}

	
	public List<File> GetAllFilesFromDirectory(String path, String arg1, String arg2){
		List<File> files = new ArrayList<File>();
		List<File> directoryFiles = new ArrayList<File>();
		File[] tempDirectoryFiles = new File(path).listFiles();
		for(File item : tempDirectoryFiles){
			directoryFiles.add(item);
		}
		
		for(int i=0; i< directoryFiles.size();i++){
			if(directoryFiles.get(i).isFile() 
					&& (directoryFiles.get(i).getName().endsWith(arg1) || directoryFiles.get(i).getName().endsWith(arg2))){
				if(files.contains(directoryFiles.get(i)))
					continue;
				files.add(directoryFiles.get(i));
			}
			else{
				if(directoryFiles.get(i).isDirectory()){
					directoryFiles.addAll(GetAllFilesFromDirectory(directoryFiles.get(i).getPath(), arg1, arg2));
				}
			}
		}
		
		return files;
	}
	
	public void WriteIndexer(File input){
		try{
			ObjectMapper mapper = new ObjectMapper();
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("directIndex/" + input.getName() + ".json"), myDictionary);
			
//		    PrintWriter writer = new PrintWriter("directIndex/" + input.getName()+".txt", "UTF-8");
//		    for(Map.Entry<String, Integer> item: myDictionary.entrySet()) {
//		        writer.println(item.getKey() + " " + item.getValue());
//		    }
//		    writer.close();
		} catch (IOException e) {
		   e.printStackTrace();
		}
	}
	
	public void InvertedIndex(List<File> fileList){
		List<Map<String, String>> invertedIndexes = new ArrayList<Map<String, String>>();
		Map<String, String> tempMap = new HashMap<String, String>();
		
		StringBuilder temp = new StringBuilder();
		
		for (File file : fileList) {
			tempMap = MapIndexFile(file);
			invertedIndexes.add(tempMap);
		}
		
		for (Map<String, String> map : invertedIndexes){
			for(Map.Entry<String, String> entry : map.entrySet()){
				if(myInvertedIndexMap.containsKey(entry.getKey()))
				{
					temp.append(myInvertedIndexMap.get(entry.getKey()));
					temp.append("\t");
					myInvertedIndexMap.put(entry.getKey(), temp.append(entry.getValue()).toString());
					temp.replace(0, temp.length(), "");
				}
				else{
					myInvertedIndexMap.put(entry.getKey(), entry.getValue());
				}
			}
		}
		
		try{
		    PrintWriter invertedIndexWriter = new PrintWriter("invertedIndex.txt", "UTF-8");
		    PrintWriter mapIndexWriter = new PrintWriter("mapIndex.txt", "UTF-8");
		    for(Map.Entry<String, String> item: myInvertedIndexMap.entrySet()) {
		    	invertedIndexWriter.println(item.getKey() + "\t" + item.getValue());
		    	mapIndexWriter.println(item.getKey() + "\tinvertedIndex.txt");
		    }
		    invertedIndexWriter.close();
		    mapIndexWriter.close();
		} catch (IOException e) {
		   // do something
		}
	}
	
	public Map<String, String> MapIndexFile(File file){
		Map<String, String> indexMap = new HashMap<String, String>();
		ObjectMapper mapper = new ObjectMapper();		
		try{
			indexMap = mapper.readValue(file, new TypeReference<HashMap<String,Object>>() {});
//			FileReader fileReader = new FileReader(file);
//			BufferedReader bufferedReader = new BufferedReader(fileReader);
//			StringBuilder word1 = new StringBuilder();
//			StringBuilder word2 = new StringBuilder();
//			int character;
//			while ((character = bufferedReader.read()) != -1) {
//				char c = (char)character;
//				if(Character.isLetter(c)){
//					word1.append(c);
//					continue;
//				}
//				if(Character.isDigit(c)){
//					word2.append(c);
//					continue;
//				}
//				if(c==' '){
//					continue;
//				}
//				if(c == '\n'){
//					indexMap.put(word1.toString(), file.getName() + " " + word2);
//					word1.replace(0, word1.length(), "");
//					word2.replace(0, word2.length(), "");
//				}
//			}
//			fileReader.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		return indexMap;
	}
}
