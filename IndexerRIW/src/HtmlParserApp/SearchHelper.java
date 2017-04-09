package HtmlParserApp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SearchHelper {

	public String[] userInput;	
	List<MapHelper> resultList = new ArrayList<MapHelper>();
	
	public Porter porter;
	
	public SearchHelper(Porter p){
		this.porter = p;
	}
	
	/*
	 * Se citeste input-ul utilizatorului si pentru fiecare cuvant din input se aplica algoritmul Porter pentru a obtine forma canonica a acestuia.
	 * */
	public void ReadUserInput(){
		System.out.println("Cauta: ");
		Scanner input = new Scanner(System.in);
		String userInputString = input.nextLine();
		input.close();
		userInput = userInputString.split(" ");
		
		for(int i=0; i<userInput.length; i++){
			userInput[i] = porter.NormalForm(userInput[i]);
		}	
	}
	
	/*
	 * Metoda care populeaza o lista cu documentele care indeplinesc criteriul de cautare(sa contina macat unul din cuvintele date de utilizator).
	 * */
	public void BooleanSearch(Helper helper){
		ReadUserInput();
		
		Map<String, List<MapHelper>> map = new HashMap<String, List<MapHelper>>();
		List<MapHelper> tempResultList = new ArrayList<MapHelper>();
		List<MapHelper> currentResultList = new ArrayList<MapHelper>();
		ObjectMapper mapper = new ObjectMapper();	
		Boolean ok = true;
		
		try{
			map = mapper.readValue(new File("invertedIndex.json"), new TypeReference<HashMap<String, List<MapHelper>>>() {});
			for(Map.Entry<String, List<MapHelper>> entry : map.entrySet()){
				for(String word : userInput){
					if(word.startsWith("+")){
						word = word.substring(1);
						if(entry.getKey().equals(word)){
							currentResultList.addAll(entry.getValue());
							if(!resultList.isEmpty()){
								for(MapHelper currentItem : currentResultList){
									for(MapHelper currentTemp : resultList){
										if(currentItem.Equals(currentTemp))
											tempResultList.add(currentTemp);
									}
								}
								resultList = tempResultList;
							}
							else{
								resultList = currentResultList;
							}
						}
						continue;
					}						
					if(word.startsWith("-")){
						//userInput = ArrayUtils.removeElement(userInput, word);
						word = word.substring(1);
						if(entry.getKey().equals(word)){
							currentResultList.addAll(entry.getValue());
							if(!resultList.isEmpty()){
								for(MapHelper currentTemp : resultList){
									ok = true;
									for(MapHelper currentItem : currentResultList){
										if(currentItem.Equals(currentTemp))
											ok = false;										
									}
									if(ok)
										tempResultList.add(currentTemp);
								}
								resultList = tempResultList;
							}else{
								//resultList = currentResultList;
							}
						}
						continue;
					}						
					if(entry.getKey().equals(word))
						resultList.addAll(entry.getValue());
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
