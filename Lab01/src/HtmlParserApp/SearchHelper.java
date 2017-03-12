package HtmlParserApp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SearchHelper {

	public StringBuilder userInput;
	public StringBuilder result;
	List<StringBuilder> operatori = new ArrayList<StringBuilder>();
	List<StringBuilder> operanzi = new ArrayList<StringBuilder>();	
	
	public void ReadUserInput(){
		System.out.println("Cauta: ");
		Scanner input = new Scanner(System.in);
		userInput.append(input.nextLine());
		input.close();
		FormatUserInput();
	}
	
	public void FormatUserInput(){
		StringBuilder tempOperator = new StringBuilder();
		StringBuilder tempOperand = new StringBuilder();
		char c;
		
		for(int i=0; i<userInput.length(); i++){
			c = userInput.charAt(i);
			if(Character.isLetter(c)){
				tempOperand.append(c);
				if(tempOperator != null){
					operatori.add(tempOperator);
					tempOperator.replace(0, tempOperator.length(), "");
				}
				continue;
			}
			if(!Character.isLetter(c)){
				tempOperator.append(c);
				if(tempOperand != null){
					operanzi.add(tempOperand);
					tempOperand.replace(0, tempOperand.length(), "");
				}
				continue;
			}
		}
	}
	
	public void BooleanSearch(Helper helper){
		ReadUserInput();
		try{	
			FileReader fileReader = new FileReader("invertedIndex.txt");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			List<StringBuilder> wordList = new ArrayList<StringBuilder>();
			StringBuilder word = new StringBuilder();
			StringBuilder line = new StringBuilder();
			char c;
			while((line.append(bufferedReader.readLine()))!= null){
				for(int i=0;i <line.length();i++){
					c=line.charAt(i);
					if(Character.isLetter(c)){
						word.append(c);
					}
					else{
						wordList.add(word);
						word.replace(0, word.length(), "");
						break;
					}
				}
				line.replace(0, line.length(), "");
			}
			
			for(StringBuilder sb : operatori){
				if(helper.stopWords.contains(sb.toString().toLowerCase()))
					continue;
//				if(helper.exceptionWords.contains(sb.toString())){
//					
//				}
				if(wordList.contains(sb)){
					//Get the line of the sb word and take only the docs
					result.append("the rest of the line");
				}
			}
			
			bufferedReader.close();
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		
	}
}
