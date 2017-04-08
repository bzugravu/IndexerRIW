package HtmlParserApp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
	"count",
	"tf"
})
public class WordFrequency {
	
	@JsonProperty("count")
	public int count;
	
	@JsonProperty("tf")
	public double tf;
	
	public WordFrequency(){
		
	}
	
	public WordFrequency(int nr, double tf) {
		this.count = nr;
		this.tf = tf;
	}
	
	@JsonProperty("count")
	public void SetCount(int nr){
		this.count = nr;
	}
	
	@JsonProperty("count")
	public int GetCount(){
		return this.count;
	}
	
	@JsonProperty("tf")
	public void SetTf(double tf){
		this.tf = tf;
	}
	
	@JsonProperty("tf")
	public double GetTf(){
		return this.tf;
	}
}
