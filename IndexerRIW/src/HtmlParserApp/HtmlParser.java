package HtmlParserApp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {
	public Document ReadHtmlFile(File input) throws IOException{
		return Jsoup.parse(input, "UTF-8");
	}
	
	public String GetTitle(Document doc){
		return doc.title();
	}
	
	public String GetText(Document doc){
		doc.select("a").remove();
		return doc.body().text();
	}
	
	public List<String> GetAttributeContent(Document doc, String tag, String value){
		List<String> attContent = new ArrayList<String>();
		Elements tags = doc.getElementsByTag(tag);
		
		for(Element item : tags){
			if(item.attr("name").equals(value)){
				attContent.add(item.attr("content"));
			}
		}
		
		return attContent;
	}
	
	public List<String> GetAnchors(Document doc){
		List<String> anchors = new ArrayList<String>();
		Elements tags = doc.getElementsByTag("a");
		
		for(Element item : tags){
			if(item.attr("href").isEmpty() || item.attr("href").contains("#"))
				continue;
			anchors.add(item.attr("href"));
		}
		
		return anchors;
	}
}
