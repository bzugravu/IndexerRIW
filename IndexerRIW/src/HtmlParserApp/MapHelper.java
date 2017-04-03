package HtmlParserApp;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
	"file",
	"count"
})
public class MapHelper {
	@JsonProperty("file")
	public String file;
	
	@JsonProperty("count")
	public int count;
	
	public MapHelper(){
		
	}
	
	public MapHelper(File input, int nr){
		this.file = input.getName();
		this.count = nr;
	}
	
	@JsonProperty("file")
	public void SetFile(String file){
		this.file = file;
	}
	
	@JsonProperty("file")
	public String GetFile(){
		return this.file;
	}
	
	@JsonProperty("count")
	public void SetCount(int nr){
		this.count = nr;
	}
	
	@JsonProperty("count")
	public int GetCount(){
		return this.count;
	}
	
	@JsonIgnore
	public Boolean Equals(MapHelper mh){
		return (this.file.equals(mh.file) && this.count == mh.count) ? true : false;
	}
}
