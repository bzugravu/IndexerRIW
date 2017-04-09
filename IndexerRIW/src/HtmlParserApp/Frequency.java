package HtmlParserApp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Frequency {
	
	public List<MapHelper> fileList = new ArrayList<MapHelper>();
	Map<String, List<MapHelper>> map = new HashMap<String, List<MapHelper>>();
	//Map<String, Double> idfMap = new HashMap<String, Double>();
	static Map<String, Double> idfMap = new ConcurrentHashMap<String, Double>();
	//Map<String, Double[]> tfMap = new HashMap<String, Double[]>();
	static Map<String, Double[]> tfMap = new ConcurrentHashMap<String, Double[]>();
	Map<String, Double> distance = new HashMap<String, Double>();
	public String[] searchQuery;	
	Double[] query;
	
	public Frequency(SearchHelper sh, Helper h){
		sh.BooleanSearch(h);		
		fileList = sh.resultList;
		searchQuery = sh.userInput;
		InitializeMapper();
	}
	
	public void InitializeMapper(){
		ObjectMapper mapper = new ObjectMapper();
		try{
			map = mapper.readValue(new File("invertedIndex.json"), new TypeReference<HashMap<String, List<MapHelper>>>() {});
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/*
	 * Functie ce populeaza un map cu elemente de tip "vector document", ce contine metrici pentru fiecare cuvant din document.
	 * Se parseaza fiecare document, iar pentru acestea se adauga in map cate un element.
	 * Elementul din map corespunde unui singur fisier, si contine informatii despre cuvintele prezente in fisier(tf * idf).
	 * */
	public void TermFrequency(){
		ExecutorService termExecutor = Executors.newFixedThreadPool(5);
		int nrWords = searchQuery.length;
				
		for(MapHelper file : fileList){
			try{
				Runnable worker = new TermFrequencyThread(file, nrWords, map, searchQuery, idfMap);
				termExecutor.execute(worker);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		termExecutor.shutdown();
        while (!termExecutor.isTerminated()) {        	
        } 
	}
	
	/*
	 * Functie care populeaza un map cu valoarea idf pentru fiecare cuvant din query.
	 * Daca nu gaseste un cuvant din query in fisierul de indexare, valoarea idf a acestuia va fi 0.
	 * */
	public void DocumentFrequency(){	
		ExecutorService docExecutor = Executors.newFixedThreadPool(5);
		
		for(String word : searchQuery){
			try{
				Runnable worker = new DocumentFrequencyThread(word, map);
				docExecutor.execute(worker);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		docExecutor.shutdown();
        while (!docExecutor.isTerminated()) {        	
        } 
	}
	
	/*
	 * Functie care construieste vectorul corespunzator query-ului.
	 * */
	public void SetQueryParams(){
		int wordCount = searchQuery.length;
		this.query = new Double[wordCount];
		int index = 0;
		
		for(String word : searchQuery){
			query[index] = ((double)1 / wordCount) * idfMap.get(word);
			index++;
		}
	}
	
	/*
	 * Functie care calculeaza cosinusul dintre fisierele rezultate in urma cautarii booleene si vectorul corespunzator query-ului.
	 * */
	public void SetDistances(){
		for(Map.Entry<String, Double[]> document : tfMap.entrySet()){		
			double vectMultiplySum = 0;
			double moduleQuerySum = 0;
			double moduleDocSum = 0;
			
			for(int i=0; i < query.length; i++){
				vectMultiplySum += query[i] * document.getValue()[i];
				moduleQuerySum += query[i] * query[i];
			}
			
			for(int i=0; i < document.getValue().length; i++){
				if(document.getValue()[i] != null)
					moduleDocSum += document.getValue()[i] * document.getValue()[i];
			}
			
			double dist = vectMultiplySum / (Math.sqrt(moduleDocSum) * Math.sqrt(moduleQuerySum));
			distance.put(document.getKey(), dist);
		}
	}
}

