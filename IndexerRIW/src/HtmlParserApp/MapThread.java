package HtmlParserApp;

import java.io.File;

public class MapThread implements Runnable {

	private Helper helper;
	private File file;
	
	public MapThread(File fileName, Helper helperName){
		this.helper = new Helper();
		this.file = fileName;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		File input = new File(file.getPath());
		try{
			helper.PopulateDictionary(input);
			helper.WriteIndexer(file);
			System.out.println("Thread for "+file.getName()+" has finished processing");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
