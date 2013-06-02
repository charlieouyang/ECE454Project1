package justen;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;

import Data.PropertiesOfPeer;
import Main.Peer;

public class Status implements Serializable{

	private int numFiles;
	float[] local;
	float[] system;
	int[] leastReplication;
	float[] weightedLeastReplication;
	HashSet<String> allChunks;
	
	Hashtable<String, Integer> allCompletedFiles;
	Hashtable<String, TorrentMetaData> allMetaData;
	
	Hashtable<String, Integer> fileNameIndexMap;
	
	
	public Status(ConcurrencyManager cm) {
		super();
		HashSet<TorrentFile> completedFiles = cm.getAllFiles();
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
			
			int[] replicatedChunks = new int[numberOfChunks];
			int localNumChunks = 0, systemNumChunks = 0;
			
			for (int k = 0; k < numberOfChunks; k++) {
				if (cm.getIncompleteChunks().contains(tFile.getChunkName(k))) {
					localNumChunks++;
					replicatedChunks[k]++;
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
	
	public boolean containsChunk(String chunkName) {
		return allChunks.contains(chunkName);
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
