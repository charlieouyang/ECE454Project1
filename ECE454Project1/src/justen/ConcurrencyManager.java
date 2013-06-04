package justen;

import java.io.File;
import java.util.HashSet;
import java.util.Hashtable;

public class ConcurrencyManager {

	private FileManager fileManager;
	private String peerName;
	private HashSet<TorrentFile> allFiles;
	private Hashtable<String, String> allLocalChunkMap;
	private Hashtable<String, TorrentMetaData> allMetaData;

	
	public ConcurrencyManager(String peerName) {
		this.peerName = peerName;
		fileManager = new FileManager(peerName);
		allFiles = fileManager.getCompletedFiles();
		allLocalChunkMap = fileManager.getAllLocalChunks();
		allMetaData = new Hashtable<String, TorrentMetaData>();
	}
	
	public synchronized void refreshConcurrencyManager() {
		allFiles = fileManager.getCompletedFiles();
		allLocalChunkMap = fileManager.getAllLocalChunks();
	}
	
	public synchronized Hashtable<String, TorrentMetaData> getAllMetaData() {
		return allMetaData;
	}
	
	public synchronized void updateMetaData(String key, TorrentMetaData value) {
		allMetaData.put(key, value);
	}
	
	public synchronized HashSet<TorrentFile> getAllFiles() {
		refreshConcurrencyManager();
		return allFiles;
	}
	
	public synchronized Hashtable<String, String> getIncompleteChunks() {
		refreshConcurrencyManager();
		return allLocalChunkMap;
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
		allMetaData.put(tFile.getFileName(), new TorrentMetaData(tFile.getFileName(), (int)tFile.getNumberOfChunks()));
		
		return 0;
	}
	
	public synchronized byte[] getChunkData(String fileName, int chunkNum) {
		if (fileManager.hasCompleteFile(fileName))
			return fileManager.getChunk(fileName, chunkNum);
		else if (fileManager.hasChunk(fileName, chunkNum))
			return fileManager.getChunkFromIncompleteFile(fileName, chunkNum);			
		else
			return null;
	}
	
	public synchronized boolean writeChunk(String fileName, int chunkNum, byte[] data) {
		if (chunkNum >= allMetaData.get(fileName).getNumberOfChunks())
			return false;
		
		try {
			fileManager.writeChunkToMemory(allMetaData.get(fileName), chunkNum, data);	
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
}
