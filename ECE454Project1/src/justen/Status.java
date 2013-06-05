package justen;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import Data.PropertiesOfPeer;
import Main.Peer;

public class Status implements Serializable{

	public int numFiles;
	public float[] local;
	public float[] system;
	public int[] leastReplication;
	public float[] weightedLeastReplication;
	public Hashtable<String, String> allChunks;
	public HashSet<TorrentFile> allFiles;
	public Hashtable<String, Integer> allCompletedFiles; // <filename, #chunks>
	public Hashtable<String, TorrentMetaData> allMetaData;
	
	public Hashtable<String, Integer[]> fileNameChunkReplicationMap;
	
	
	public Status(ConcurrencyManager cm) {
		super();
		HashSet<TorrentFile> completedFiles = cm.getAllFiles();
		allFiles = completedFiles;
//		numFiles = completedFiles.size();
		allCompletedFiles = new Hashtable<String, Integer>();
		allMetaData = cm.getAllMetaData(); 
		fileNameChunkReplicationMap = new Hashtable<String, Integer[]>();
		numFiles = allMetaData.size();
		
		Hashtable<String, Status> otherPeerStatusMap = PropertiesOfPeer.listOfOtherPeersStatus;
		
		for (TorrentFile t : completedFiles) {
			allCompletedFiles.put(t.getFileName(), (int)t.getNumberOfChunks());
		}
		
		allChunks = cm.getIncompleteChunks(); // hashtable
		
		
		if (numFiles == 0)
			return;
		
		TorrentMetaData[] filesMetaData = allMetaData.values().toArray(new TorrentMetaData[allMetaData.size()]);
		Set<String> chunkFiles = allChunks.keySet();
		for (int i =0; i < filesMetaData.length; i++) {
			Integer[] chunkReplicationArray = new Integer[filesMetaData[i].getNumberOfChunks()];
			
			for (int a = 0; a < chunkReplicationArray.length; a++) 
				chunkReplicationArray[a] = 0;
			
			boolean containedInAllFiles = false;
			for (TorrentFile t : allFiles) {
				if (t.getFileName().equals(filesMetaData[i].getFileName())) {
					containedInAllFiles = true;
					break;
				}
			}
			if (containedInAllFiles) { // we have the seeder
				// increase all by 1
				for (int j = 0; j < chunkReplicationArray.length; j++) {
					chunkReplicationArray[j]++;
					
					// now go through all other peers and find if chunk exists.
					for (Entry<String, Status> e : otherPeerStatusMap.entrySet()) {
						if (e.getValue().containsChunk(filesMetaData[i].getChunkName(j))) {
							chunkReplicationArray[j]++;
						}
					}
				}
				
			} else if (chunkFiles.contains(filesMetaData[i].getFileName())) { // we are leeching
				// only increase the chunks we have
				for (int j = 0; j < chunkReplicationArray.length; j++) {
					if (allChunks.contains(filesMetaData[i].getChunkName(j))) {
						chunkReplicationArray[j]++;
					}
					
					// now go through all other peers and find if chunk exists.
					for (Entry<String, Status> e : otherPeerStatusMap.entrySet()) {
						if (e.getValue().containsChunk(filesMetaData[i].getChunkName(j))) {
							chunkReplicationArray[j]++;
						}
					}
				}
			} else { // we have no chunks yet, but have knowledge of the file
				// go through all other peers and find if chunk exists
				for (int j = 0; j < chunkReplicationArray.length; j++) {
					for (Entry<String, Status> e : otherPeerStatusMap.entrySet()) {
						if (e.getValue().containsChunk(filesMetaData[i].getChunkName(j))) {
							chunkReplicationArray[j]++;
						}
					}
				}
			}
			fileNameChunkReplicationMap.put(filesMetaData[i].getFileName(), chunkReplicationArray);
		}
		
//		local = new float[numFiles];
//		system = new float[numFiles];
//		leastReplication = new int[numFiles];
//		weightedLeastReplication = new float[numFiles];
//		int totalNumberOfChunks = 0;
//		
//		TorrentFile[] allFilesArray = completedFiles.toArray(new TorrentFile[completedFiles.size()]);
//		for (int i = 0; i < numFiles; i++) {
//			TorrentFile tFile = allFilesArray[i];
//			
//			
//			int numberOfChunks = (int)tFile.getNumberOfChunks();
//			totalNumberOfChunks += numberOfChunks;
//
//			int localNumChunks = 0, systemNumChunks = 0;
//			int[] replicatedChunks = new int[numberOfChunks];
//			
//			//Added if statement to skip loop if there's no chunks... 
//			//GOTTA CHECK IF THIS IS CORRECT
//			if (cm.getIncompleteChunks() != null) {
//				for (int k = 0; k < numberOfChunks; k++) {
//					
//					Hashtable tempHash = cm.getIncompleteChunks();
//					String tempName = tFile.getChunkName(k);
//					
//					if (cm.getIncompleteChunks()
//							.contains(tFile.getChunkName(k))) {
//						localNumChunks++;
//						systemNumChunks++;
//						replicatedChunks[k]++;
//					}
//					for (Entry<String, Status> e : otherPeerStatusMap.entrySet()) {
//						if (e.getValue().containsChunk(tFile.getChunkName(k))) {
//							systemNumChunks++;
//							replicatedChunks[k]++;
//						}
//					}
//					// if peer has this chunk
//					// systemNumChunks++
//					// replicatedChunks[k]++
//					// iterate through the map and check
//				}
//			}
//			
//			local[i] = (float) localNumChunks / numberOfChunks;
//			system[i] = (float) systemNumChunks / numberOfChunks;
//			leastReplication[i] = replicatedChunks[0];
//			
//			for (int j = 1; j < replicatedChunks.length; j++) {
//				if (leastReplication[i] > replicatedChunks[j])
//					leastReplication[i] = replicatedChunks[j];
//			}			
//		}
//		
//		for (int a = 0; a < numFiles; a++) {
//			TorrentFile tFile = allFilesArray[a];
//			weightedLeastReplication[a] = (float) totalNumberOfChunks / tFile.getNumberOfChunks();
//		}
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
	
	public Hashtable<String, Integer[]> getFileNameChunkReplicationMap(){
		return fileNameChunkReplicationMap;
	}
}
