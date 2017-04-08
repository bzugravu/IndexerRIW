package HtmlParserApp;

import java.util.ArrayList;
import java.util.List;

public class Porter {
	
	public List<Character> vowels = new ArrayList<Character>();
	
	public void InitializeVowels(){
		this.vowels = new ArrayList<Character>();
		this.vowels.add('a');
		this.vowels.add('e');
		this.vowels.add('i');
		this.vowels.add('o');
		this.vowels.add('u');
	}
	
	public Porter(){
		InitializeVowels();
	}
	
	public String NormalForm(String wordToConvert){
		//InitializeVowels();
		String result = wordToConvert;
		WordMap wm = new WordMap();
		
		wm = GetWordMapping(wordToConvert);		
		
		//Porter steps
		result = FirstStepA(result);
		wm = GetWordMapping(result);
		result = FirstStepB(result, wm);
		wm = GetWordMapping(result);
		result = FirstStepC(result, wm);
		result = SecondStep(result);
		result = ThirdStep(result);
		result = ForthStep(result);
		result = FifthStepA(result);
		wm = GetWordMapping(result);
		result = FifthStepB(result, wm);
		
		return result;
	}
	
	public WordMap GetWordMapping(String word){
		WordMap wm = new WordMap();
		StringBuilder wordForm = new StringBuilder();
		
		for(int i=0; i<word.length();i++){
			if((i > 0) && word.charAt(i) == 'y' && !this.vowels.contains(word.charAt(i))){
				wordForm.append('V');
				continue;
			}
			if(vowels.contains(word.charAt(i))){
				wordForm.append('V');
				continue;
			}
			else{
				wordForm.append('C');
				continue;
			}
		}
		
		wm.word = SimplifyWordMapForm(wordForm);
		wm.m = GetPairsNumber(wm.word);
		
		return wm;
	}
	
	public String SimplifyWordMapForm(StringBuilder sb){
		StringBuilder result = new StringBuilder();
		
		for(int i=0;i<sb.length();i++){
			if(i>0){
				if(result.charAt(result.length() - 1) == sb.charAt(i))
					continue;
				else
					result.append(sb.charAt(i));
			}
			else
				result.append(sb.charAt(i));
		}
		
		return result.toString();
	}
	
	public int GetPairsNumber(String s){
		int result = 0;
		
		if(s.length() == 1)
			return result;
		
		for(int i=1;i<s.length();i++){
			if(s.charAt(i) == 'C' && s.charAt(i-1) == 'V'){
				result++;
				//i++;
			}
		}
		
		return result;
	}
	
	public String FirstStepA(String word){
		if(word.endsWith("sses")){
			word = word.substring(0, word.length() - 2);
			return word;
		}
		
		if(word.endsWith("ies")){
			word = word.substring(0, word.length() - 2);
			return word;
		}
		
		if(word.endsWith("ss"))
			return word;
		
		if(word.endsWith("s")){
			word = word.substring(0, word.length() - 1);
			return word;
		}
		
		return word;
	}

	public String FirstStepB(String word, WordMap wm){
		try{
			String temp = wm.word.substring(1, wm.word.length() - 1);
			
			if(wm.m > 0 && word.endsWith("eed")){
				word = word.substring(0, word.length() - 1);
				return word;
			}
			
			if(temp.contains("V") && word.endsWith("ed")){
				word = word.substring(0, word.length() - 2);
				return word;
			}
			
			if(temp.contains("V") && word.endsWith("ing")){
				word = word.substring(0, word.length() - 3);
				return word;
			}
			return word;
		}
		catch(Exception ex){
			return word;
		}
	}
	
	public String FirstStepC(String word, WordMap wm){
		try{
			String temp = wm.word.substring(1, wm.word.length() - 1);
			
			if(temp.contains("V") && word.endsWith("y")){
				word = word.substring(0,word.length() - 1);
				word += "i";
				return word;
			}
			
			return word;
		}
		catch(Exception ex){
			return word;
		}
	}
	
	public String SecondStep(String word){
		if(word.endsWith("ational")){
			String temp = word.substring(0, word.length() - 7);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "ate";
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("tional")){
			String temp = word.substring(0, word.length() - 6);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "tion";
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("enci")){
			String temp = word.substring(0, word.length() - 4);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "ence";
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("anci")){
			String temp = word.substring(0, word.length() - 4);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "ance";
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("izer")){
			String temp = word.substring(0, word.length() - 4);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "ize";
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("abli")){
			String temp = word.substring(0, word.length() - 4);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "able";
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("alli")){
			String temp = word.substring(0, word.length() - 4);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "al";
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("entli")){
			String temp = word.substring(0, word.length() - 5);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "ent";
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("eli")){
			String temp = word.substring(0, word.length() - 3);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "e";
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("ousli")){
			String temp = word.substring(0, word.length() - 5);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "ous";
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("ization")){
			String temp = word.substring(0, word.length() - 7);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "ize";
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("ation")){
			String temp = word.substring(0, word.length() - 5);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "ate";
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("ator")){
			String temp = word.substring(0, word.length() - 4);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "ate";
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("alism")){
			String temp = word.substring(0, word.length() - 5);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "al";
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("iveness")){
			String temp = word.substring(0, word.length() - 7);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "ive";
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("fulness")){
			String temp = word.substring(0, word.length() - 7);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "ful";
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("ousness")){
			String temp = word.substring(0, word.length() - 7);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "ous";
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("aliti")){
			String temp = word.substring(0, word.length() - 5);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "al";
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("iviti")){
			String temp = word.substring(0, word.length() - 5);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "ive";
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("biliti")){
			String temp = word.substring(0, word.length() - 6);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "ble";
				return temp;
			}
			return word;
		}
		
		return word;
	}
	
	public String ThirdStep(String word){
		if(word.endsWith("icate")){
			String temp = word.substring(0, word.length() - 5);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "ic";
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("ative")){
			String temp = word.substring(0, word.length() - 5);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("alize")){
			String temp = word.substring(0, word.length() - 5);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "al";
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("iciti")){
			String temp = word.substring(0, word.length() - 5);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "ic";
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("ical")){
			String temp = word.substring(0, word.length() - 4);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				temp += "ic";
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("ful")){
			String temp = word.substring(0, word.length() - 3);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){
				return temp;
			}
			return word;
		}
		if(word.endsWith("ness")){
			String temp = word.substring(0, word.length() - 4);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 0){		
				return temp;
			}
			return word;
		}
		
		return word;
	}
	
	public String ForthStep(String word){
		if(word.endsWith("al")){
			String temp = word.substring(0, word.length() - 2);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 1){
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("ance")){
			String temp = word.substring(0, word.length() - 4);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 1){
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("ence")){
			String temp = word.substring(0, word.length() - 4);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 1){
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("er")){
			String temp = word.substring(0, word.length() - 2);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 1){
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("ic")){
			String temp = word.substring(0, word.length() - 2);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 1){
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("able")){
			String temp = word.substring(0, word.length() - 4);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 1){
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("ible")){
			String temp = word.substring(0, word.length() - 4);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 1){
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("ant")){
			String temp = word.substring(0, word.length() - 3);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 1){
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("ement")){
			String temp = word.substring(0, word.length() - 5);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 1){
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("ment")){
			String temp = word.substring(0, word.length() - 4);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 1){
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("ent")){
			String temp = word.substring(0, word.length() - 3);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 1){
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("ion")){
			String temp = word.substring(0, word.length() - 3);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 1 && (temp.endsWith("t") || temp.endsWith("s"))){
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("ou")){
			String temp = word.substring(0, word.length() - 2);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 1){
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("ism")){
			String temp = word.substring(0, word.length() - 3);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 1){
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("ate")){
			String temp = word.substring(0, word.length() - 3);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 1){
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("iti")){
			String temp = word.substring(0, word.length() - 3);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 1){
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("ous")){
			String temp = word.substring(0, word.length() - 3);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 1){
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("ive")){
			String temp = word.substring(0, word.length() - 3);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 1){
				return temp;
			}
			return word;
		}
		
		if(word.endsWith("ize")){
			String temp = word.substring(0, word.length() - 3);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 1){
				return temp;
			}
			return word;
		}
		
		return word;
	}

	public String FifthStepA(String word){
		if(word.endsWith("e")){
			String temp = word.substring(0, word.length() - 1);
			WordMap wm = GetWordMapping(temp);
			if(wm.m > 1){
				return temp;
			}
			
			if(wm.m == 1 && !wm.word.endsWith("CVC")){
				return temp;
			}
			return word;
		}	
		
		return word;
	}

	public String FifthStepB(String word, WordMap wm){
		if(wm.m > 1 && word.endsWith("ll")){
			word = word.substring(0, word.length() - 1);
		}
		
		return word;
	}
}
