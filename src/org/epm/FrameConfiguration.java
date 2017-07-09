package org.epm;

import java.io.File;
import java.util.ArrayList;

public class FrameConfiguration {
	private File configFile;
	private File pictureFolder;
	private ArrayList<File> pictureArray = new ArrayList<File>();
	
	
	public void listFilesForFolder(String configFilename) {
		File folder = new File(configFilename);
		pictureArray = new ArrayList<File>();
		
	    for (File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	System.out.println("\n*** Folder Found: " + fileEntry.getPath() + "************************************************");
	            listFilesForFolder(fileEntry.getPath());
	        } else {
	            pictureArray.add(fileEntry);
	            
	        }
	    }
	}	
	public ArrayList<File> getPictureFiles() {
		return pictureArray;
	}
	
	public ArrayList<File> toConsole() {
	    
	    for (File f : pictureArray ) {
	    	System.out.println(f.getPath());
	    }
	    return pictureArray;
	}
}
