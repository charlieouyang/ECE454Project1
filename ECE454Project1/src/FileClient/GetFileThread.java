package FileClient;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import justen.Status;
import justen.TorrentMetaData;
import Data.PropertiesOfPeer;

public class GetFileThread extends Thread {
	String fileName;
	TorrentMetaData fileMettaData;
	Status peerStatus;
	
	public GetFileThread(String fileName, TorrentMetaData fileMettaData) {
		this.fileName = fileName;
		this.fileMettaData = fileMettaData;
		this.peerStatus = PropertiesOfPeer.getCurrentPeerStatus();
	}

	@Override
	public void run() {
		try {
			// Manage all of the chunk requests and chunk to host map
			Hashtable <Integer, String> chunkToHostMapping = new Hashtable<Integer, String>();
			int numberOfChunks = fileMettaData.getNumberOfChunks();
			
			for (int i = 0; i < numberOfChunks; i++) {
				//Get the chunk# to host mapping for chunk requests
				chunkToHostMapping.put(i, getChunkToHostMapping(fileName, i));
			}
			
			//Start sending out chunk request
			Iterator<Map.Entry<Integer, String>> mappingIterator = chunkToHostMapping.entrySet().iterator();
			while (mappingIterator.hasNext()) {
				Map.Entry<Integer, String> entry = mappingIterator.next();
				
				RequestForChunk requestForChunkThread = new RequestForChunk(fileName, entry.getValue(), entry.getKey());
				requestForChunkThread.start();
			}
			
		} catch (Exception e) {
			System.err.println("Screwed up on check for new files to get");
			e.printStackTrace();
		}
	}
	
	public String getChunkToHostMapping(String fileName, int chunkNumber){
		String hostName = "";
		Hashtable<String, Integer[]> fileNameChunkReplicationMap = peerStatus.fileNameChunkReplicationMap;
		
		
		
		return hostName;
	}
}