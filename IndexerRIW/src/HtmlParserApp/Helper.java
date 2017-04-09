package HtmlParserApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
	
	public Map<String, WordFrequency> myDictionary = new HashMap<String, WordFrequency>();
	
	public Map<String, List<MapHelper>> myInvertedIndexMap = new HashMap<String, List<MapHelper>>();
	
	public Porter porter;
	
	public Helper(Porter p){
		this.porter = p;
	}
	
	/*
	 * Se initializeaza lista de stop-words
	 * */
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
	
	/*
	 * Se initializeaza lista de exceptii
	 * */
	public void SetExceptionWordsList(){
		try {
			File file = new File("exceptionWords.txt");
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
	
	/*
	 * Se parseaza fiecare fisier caracter cu caracter.
	 * In momentul in care s-a creat un cuvant, se verifica daca face parte din list de exceptii sau din lista de stop-words.
	 * Daca trece de pasii anteriori, se normalizeaza cuvantul cu ajutorul algoritmului lui Porter si se adauga intr-un map care contine 
	 * perechi cheie-valoare de tipul cuvant-numar aparitii cuvant.
	 * */
	public void PopulateDictionary(File input) throws IOException{
		myDictionary.clear();
		HtmlParser htmlParser = new HtmlParser();
		Document doc = htmlParser.ReadHtmlFile(input);
		String text = htmlParser.GetText(doc);
		StringBuilder word = new StringBuilder();
		int wordsCount = 0;
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
					
					String wordForm = word.toString().toLowerCase();
					wordForm = porter.NormalForm(wordForm);
					if(!wordForm.isEmpty()){					
						if(myDictionary.containsKey(wordForm)){
							WordFrequency tempWF = myDictionary.get(wordForm);
							tempWF.SetCount(tempWF.count + 1);
							myDictionary.put(wordForm, tempWF);
						}
						else{
							WordFrequency tempWF = new WordFrequency(1, 0);
							myDictionary.put(wordForm, tempWF);
						}	
						
						wordsCount++;
					}
				}
				
				word.replace(0, word.length(), "");
			}
		}
		
		for(Map.Entry<String, WordFrequency> entry : myDictionary.entrySet()){
			WordFrequency tempWF = entry.getValue();
			tempWF.SetTf((double) tempWF.count / wordsCount);
			myDictionary.put(entry.getKey(), tempWF);
		}
	}
	
	/*
	 * Metoda care returneaza toata fisierele cu o anumita extensie dintr-un anumit director.
	 * Calea catre director si extensiile posibile se trimit ca parametri
	 * */
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
	
	/*
	 * Dupa ce s-a creat map-ul cu lista de cuvinte dintr-un fisier se scrie pe disc un fisier care sa contina informatiile din map-ul respectiv.
	 * */
	public void WriteIndexer(File input){
		try{
			ObjectMapper mapper = new ObjectMapper();
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("directIndex/" + input.getName() + ".json"), myDictionary);			
		} catch (IOException e) {
		   e.printStackTrace();
		}
	}
	
	/*
	 * Metoda care creaza indexul invers pe baza fisierelor de index direct create anterior.
	 * Pentru fiecare fisier de pe disc se creaza un map iar pe baza inregistrarilor din acest map temporar se scrie in alt map global,
	 * care contine informatiile de la toate fisierele.
	 * Dupa ce s-au parsat toate fisierele, se scrie pe disc un fisier cu continutul map-ului global.
	 * */
	public void InvertedIndex(List<File> fileList){
		Map<String, WordFrequency> tempMap = new HashMap<String, WordFrequency>();		
		
		for (File file : fileList) {
			
			tempMap = MapIndexFile(file);
			
			for(Map.Entry<String, WordFrequency> entry : tempMap.entrySet()){
				
				List<MapHelper> lmh = new ArrayList<MapHelper>();
				WordFrequency wf = entry.getValue();
				MapHelper mh = new MapHelper(file, wf.count, 0);
				
				if(myInvertedIndexMap.containsKey(entry.getKey())){
					lmh = myInvertedIndexMap.get(entry.getKey());
				}
				
				lmh.add(mh);
				myInvertedIndexMap.put(entry.getKey(), lmh);				
			}			
		}
		
		//IDF
		for (Map.Entry<String, List<MapHelper>> entry : myInvertedIndexMap.entrySet()){
			for(MapHelper mh : entry.getValue()){
				MapHelper tempMH = mh;
				tempMH.SetIdf((double)entry.getValue().size() / fileList.size());
				entry.getValue().set(entry.getValue().indexOf(mh), tempMH);
			}
		}
		
		try{
			ObjectMapper mapper = new ObjectMapper();
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("invertedIndex.json"), myInvertedIndexMap);
		} catch (IOException e) {
		   e.printStackTrace();
		}
	}
	
	/*
	 * Functie care citeste de pe disc un fisier si returneaza un map cu informatiile gasite in acel fisier.
	 * Fisierul este dat ca parametru.
	 * Fisierul de pe disc contine informatii stocate sub forma unui index direct.
	 * */
	public Map<String, WordFrequency> MapIndexFile(File file){
		Map<String, WordFrequency> indexMap = new HashMap<String, WordFrequency>();
		ObjectMapper mapper = new ObjectMapper();	
		
		try{
			indexMap = mapper.readValue(file, new TypeReference<HashMap<String, WordFrequency>>() {});
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		return indexMap;
	}
}
