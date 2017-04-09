package HtmlParserApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * Thread care populeaza map-ul idf cu valorile fiecarui cuvant.
 * */
public class DocumentFrequencyThread implements Runnable{
	
	String word;
	Map<String, List<MapHelper>> map;
	
	public DocumentFrequencyThread(String s, Map<String, List<MapHelper>> map) {
		this.word = s;
		this.map = map;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		List<MapHelper> wordIndexFiles = new ArrayList<MapHelper>();

		wordIndexFiles = map.get(word);
		
		if(wordIndexFiles == null)
			Frequency.idfMap.put(word, 0.0);
		else
			Frequency.idfMap.put(word, wordIndexFiles.get(0).idf);	
	}

}