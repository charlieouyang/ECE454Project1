package FileClient;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import justen.TorrentMetaData;
import Data.PropertiesOfPeer;

public class GetFileThread extends Thread {
	String fileName;
	TorrentMetaData fileMettaData;
	
	public GetFileThread(String fileName, TorrentMetaData fileMettaData) {
		this.fileName = fileName;
		this.fileMettaData = fileMettaData;
	}

	@Override
	public void run() {
		try {
			// Manage all of the chunk requests and chunk to host map
			Hashtable <String, String> chunkToHostMapping = new Hashtable<String, String>();
			int numberOfChunks = fileMettaData.getNumberOfChunks();
			
			for (int i = 0; i < numberOfChunks - 1; i++) {
				chunkToHostMapping.put(fileName + "_chunk_" + i, "localhost.7000");
			}
			
			//Start sending out chunk request
			Iterator<Map.Entry<String, String>> mappingIterator = chunkToHostMapping.entrySet().iterator();
			while (mappingIterator.hasNext()) {
				Map.Entry<String, String> entry = mappingIterator.next();
				
				RequestForChunk requestForChunkThread = new RequestForChunk(entry.getValue(), entry.getKey());
				requestForChunkThread.start();
			}
			
		} catch (Exception e) {
			System.err.println("Screwed up on check for new files to get");
			e.printStackTrace();
		}
	}
}