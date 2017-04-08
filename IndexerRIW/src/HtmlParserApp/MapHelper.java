package HtmlParserApp;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
	"file",
	"count",
	"idf"
})
public class MapHelper {
	@JsonProperty("file")
	public String file;
	
	@JsonProperty("count")
	public int count;
	
	@JsonProperty("idf")
	public double idf;
	
	public MapHelper(){
		
	}
	
	public MapHelper(File input, int nr, double idf){
		this.file = input.getName();
		this.count = nr;
		this.idf = idf;
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
	
	@JsonProperty("idf")
	public void SetIdf(double nr){
		this.idf = nr;
	}
	
	@JsonProperty("idf")
	public double GetIdf(){
		return this.idf;
	}
	
	@JsonIgnore
	public Boolean Equals(MapHelper mh){
		return (this.file.equals(mh.file) && this.count == mh.count) ? true : false;
	}
	
	@JsonIgnore
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof MapHelper))return false;
	    MapHelper otherMyClass = (MapHelper)other;
	    if(this.file.equals(otherMyClass.file) && this.count == otherMyClass.count)
	    	return true;
	    return false;
	}
}
