package FileClient;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import justen.ConcurrencyManager;
import justen.Status;
import justen.TorrentMetaData;
import Data.PropertiesOfPeer;

public class GetFileThread extends Thread {
	String fileName;
	TorrentMetaData fileMettaData;
	Status peerStatus;
	boolean fileTransferComplete;
	ConcurrencyManager concurManager;
	
	public GetFileThread(String fileName, TorrentMetaData fileMettaData) {
		this.fileName = fileName;
		this.fileMettaData = fileMettaData;
		this.peerStatus = PropertiesOfPeer.getCurrentPeerStatus();
		this.concurManager = PropertiesOfPeer.peerConcurrencyManager;
		this.fileTransferComplete = false;
	}

	@Override
	public void run() {
		try {
			while (!fileTransferComplete) {
				// Keep running this until file is complete
				
				
				
				// Manage all of the chunk requests and chunk to host map
				Hashtable<Integer, String> chunkToHostMapping = new Hashtable<Integer, String>();
				int numberOfChunks = fileMettaData.getNumberOfChunks();

				/*
				 * for (int i = 0; i < numberOfChunks; i++) { //Get the chunk#
				 * to host mapping for chunk requests chunkToHostMapping.put(i,
				 * getChunkToHostMapping(fileName, i)); }
				 */

				// Get the chunk# to host mapping for chunk requests
				chunkToHostMapping.put(i, getChunkToHostMapping(fileName));

				// Start sending out chunk request
				Iterator<Map.Entry<Integer, String>> mappingIterator = chunkToHostMapping
						.entrySet().iterator();
				while (mappingIterator.hasNext()) {
					Map.Entry<Integer, String> entry = mappingIterator.next();

					RequestForChunk requestForChunkThread = new RequestForChunk(
							fileName, entry.getValue(), entry.getKey());
					requestForChunkThread.start();
				}
			}
		} catch (Exception e) {
			System.err.println("Screwed up on check for new files to get");
			e.printStackTrace();
		}
	}
	
	public Hashtable<Integer, String> getChunkToHostMapping(String fileName){
		Hashtable<Integer, String> chunkToHostMapping = new Hashtable<Integer, String>();
		Hashtable<String, Integer[]> fileNameChunkReplicationMap = peerStatus.fileNameChunkReplicationMap;
		
		//1 Get which chunk to send first
		//2 Based on chunk, get which host we can get this chunk from
		
		return chunkToHostMapping;
	}
}