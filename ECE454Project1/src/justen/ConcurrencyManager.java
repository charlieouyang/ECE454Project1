package justen;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class ConcurrencyManager {

	private FileManager fileManager;
	private String peerName;
	private HashSet<TorrentFile> allFiles;
	
	public ConcurrencyManager(String peerName) {
		this.peerName = peerName;
		fileManager = new FileManager(peerName);
		allFiles = fileManager.getCompletedFiles();
	}
	
	public synchronized HashSet<TorrentFile> getAllFiles() {
		return allFiles;
	}
	
	public synchronized int insertFile(String fileName) {
		
		File file = new File(fileName);
		
		//check for duplicated file
		for (TorrentFile tFile : allFiles) {
			if (file.getName().equals(tFile.getFileName())) {
				// added duplicate file
				System.out.println(file.getName() + " already exists on peer: " + peerName);
				return 1;
			}
		}
		
		TorrentFile tFile = fileManager.copyFileFromRepo(fileName);
		allFiles.add(tFile);
		
		return 0;
	}
}
