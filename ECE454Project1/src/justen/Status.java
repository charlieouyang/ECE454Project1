package justen;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import Data.PropertiesOfPeer;
import Main.Peer;

public class Status implements Serializable{

	public int numFiles;
	public float[] local;
	public float[] system;
	public int[] leastReplication;
	public float[] weightedLeastReplication;
	public HashSet<String> allChunks;
	public HashSet<TorrentFile> allFiles;
	public Hashtable<String, Integer> allCompletedFiles;
	public Hashtable<String, TorrentMetaData> allMetaData;
	
	public Hashtable<String, Integer> fileNameIndexMap;
	
	
	public Status(ConcurrencyManager cm) {
		super();
		HashSet<TorrentFile> completedFiles = cm.getAllFiles();
		allFiles = completedFiles;
		numFiles = completedFiles.size();
		if (numFiles == 0)
			return;
		allCompletedFiles = new Hashtable<String, Integer>();
		fileNameIndexMap = new Hashtable<String,Integer>();
		allMetaData = cm.getAllMetaData(); 
		
		Hashtable<String, Status> temp = PropertiesOfPeer.listOfOtherPeersStatus;
		
		for (TorrentFile t : completedFiles) {
			allCompletedFiles.put(t.getFileName(), (int)t.getNumberOfChunks());
		}
		
		allChunks = cm.getIncompleteChunks();
		
		local = new float[numFiles];
		system = new float[numFiles];
		leastReplication = new int[numFiles];
		weightedLeastReplication = new float[numFiles];
		int totalNumberOfChunks = 0;
		
		TorrentFile[] allFilesArray = (TorrentFile[]) cm.getAllFiles().toArray();
		for (int i = 0; i < numFiles; i++) {
			TorrentFile tFile = allFilesArray[i];
			
			fileNameIndexMap.put(tFile.getFileName(), i);
			
			int numberOfChunks = (int)tFile.getNumberOfChunks();
			totalNumberOfChunks += numberOfChunks;

			int localNumChunks = 0, systemNumChunks = 0;
			int[] replicatedChunks = new int[numberOfChunks];
			
			for (int k = 0; k < numberOfChunks; k++) {
				if (cm.getIncompleteChunks().contains(tFile.getChunkName(k))) {
					localNumChunks++;
					systemNumChunks++;
					replicatedChunks[k]++;
				}
				for (Entry<String, Status> e : temp.entrySet()) {
					if(e.getValue().containsChunk(tFile.getChunkName(k))) {
						systemNumChunks++;
						replicatedChunks[k]++;
					}
				}
					// if peer has this chunk
						// systemNumChunks++
						// replicatedChunks[k]++
				// iterate through the map and check 
			}
			
			local[i] = (float) localNumChunks / numberOfChunks;
			system[i] = (float) systemNumChunks / numberOfChunks;
			leastReplication[i] = replicatedChunks[0];
			
			for (int j = 1; j < replicatedChunks.length; j++) {
				if (leastReplication[i] > replicatedChunks[j])
					leastReplication[i] = replicatedChunks[j];
			}			
		}
		
		for (int a = 0; a < numFiles; a++) {
			TorrentFile tFile = allFilesArray[a];
			weightedLeastReplication[a] = (float) totalNumberOfChunks / tFile.getNumberOfChunks();
		}
	}
	
	//justen_chunk_1
	public boolean containsChunk(String chunkName) {
		boolean contains = false;
		String fileName;
		
		int secondLastUnderScore = chunkName.lastIndexOf("_", chunkName.length() - 6);
		fileName = chunkName.substring(0, secondLastUnderScore);
		
		if (allChunks.contains(chunkName))
			return true;
		
		for (TorrentFile t : allFiles) {
			if (t.getFileName().equals(fileName))
				return true;
		}
		
		return false;
	}
	
	public int numberOfFiles()
	{
		return numFiles;
	}
	
	/**
	 * @param fileNumber
	 * @return Fraction of the file present locally (chunks on peer/total number of chunks in file)
	 */
	public float fractionPresentLocally(int fileNumber)
	{
		if (fileNumber < 0 || fileNumber > numFiles)
			return -1;
		return local[fileNumber];
	}
	
	/**
	 * @param fileNumber
	 * @return Fraction of file present in the system (chunks in the system/total number of chunks in the file)
	 */
	public float fractionPresent(int fileNumber)
	{
		if (fileNumber < 0 || fileNumber > numFiles)
			return -1;
		return system[fileNumber];
	}
	
	public int minimumReplicationLevel(int fileNumber)
	{
		if (fileNumber < 0 || fileNumber > numFiles)
			return -1;
		return leastReplication[fileNumber];
	}
	
	public float averageReplicationLevel(int fileNumber)
	{
		if (fileNumber < 0 || fileNumber > numFiles)
			return -1;
		return weightedLeastReplication[fileNumber];
	}
}
