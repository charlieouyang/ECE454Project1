package justen;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class DirectoryHelper {
	
	public static boolean createAllDirectories(String peerName) {
		return createDirectory(peerName) &&
				createCompletedDirectory(peerName) &&
				createChunkDirectory(peerName);
	}
	
	public static boolean createDirectory(String name)
	{
		File dir = new File(name);
		if (dir.isDirectory())
			deleteFolder(dir);
		return dir.mkdir();
	}
	
	public static boolean createCompletedDirectory(String name)
	{
		File dir = new File(name + "\\completed");
		if (dir.isDirectory())
			deleteFolder(dir);
		return dir.mkdir();
	}
	
	public static boolean createChunkDirectory(String name)
	{
		File dir = new File(name + "\\chunks");
		if (dir.isDirectory())
			deleteFolder(dir);
		return dir.mkdir();
	}
	
	public static boolean createDirectory(String name, String fileName) {
		File dir = new File(name + "\\chunks", fileName);
		if (dir.isDirectory())
			return true;
		else 
			return dir.mkdir();
	}
	
	/*
	 * Recursively deletes all files in directory
	 * */
	public static void deleteFolder(File folder)
	{
		File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}
	
	public static String getAggregateChunkList(String directory){
		File folder = new File(directory);
		File[] listOfFiles = folder.listFiles();
		
		ArrayList<Integer> listOfChunkNumbers = new ArrayList<Integer>();
		
		for (int i = 0; i < listOfFiles.length; i++) {
			//It's a file
			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName());
				
				String fileName = listOfFiles[i].getName();
				int lastUnderScore = fileName.lastIndexOf("_");
				int endOfString = fileName.lastIndexOf(".");
				
				String chunkIndexString = fileName.substring(lastUnderScore + 1, endOfString);
				int chunkIndex = Integer.parseInt(chunkIndexString);
				
				listOfChunkNumbers.add(chunkIndex);
			}
			//It's a directory
			else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());
			}
		}
		
		Collections.sort(listOfChunkNumbers);
		
		//int previousValue = 0;
		ArrayList<String> listOfRanges = new ArrayList<String>();
		
		
		int shouldBeValue = 0;
		boolean hiccup = false;
		boolean starting = true;
		boolean lastEntry = false;
		
		int lastValueTracker = 1;
		
		int startValueRange = 0;
		int lastValueRange = 0;
		
		
		for (Integer value : listOfChunkNumbers){
			if (starting){
				startValueRange = value;
				starting = false;
				shouldBeValue = value + 1;
			}
			else if (value != shouldBeValue){
				hiccup = true;
				lastValueRange = shouldBeValue - 1;
				shouldBeValue = value + 1;
				
				if (lastValueTracker == listOfChunkNumbers.size()){
					lastEntry = true;
				}
			}
			else if (lastValueTracker == listOfChunkNumbers.size()){
				hiccup = true;
				lastValueRange = value;
			}
			else{
				shouldBeValue++;
			}
			
			if (hiccup){
				listOfRanges.add(Integer.toString(startValueRange) + "-" + lastValueRange);
				hiccup = false;
				
				startValueRange = value;
			}
			if (lastEntry){
				listOfRanges.add(Integer.toString(value) + "-" + value);
			}
			
			lastValueTracker++;
		}
		
		//ArrayList<String> finalListOfRanges = new ArrayList<String>();
		String overallString = "";
		
		for (String range : listOfRanges){
			int indexOfDash = range.indexOf("-");
			String firstNumber = range.substring(0, indexOfDash);
			String secondNumber = range.substring(indexOfDash + 1, range.length());
			
			if (firstNumber.equals(secondNumber)){
				//finalListOfRanges.add(firstNumber);
				overallString = overallString + firstNumber + ",";
			}
			else{
				//finalListOfRanges.add(range);
				overallString = overallString + range + ",";
			}
		}
		
		return overallString;
		//return finalListOfRanges;
	}
}
