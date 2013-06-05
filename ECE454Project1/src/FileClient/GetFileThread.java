package FileClient;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import justen.ConcurrencyManager;
import justen.InflightChunkRequestManager;
import justen.Status;
import justen.TorrentMetaData;
import Data.PropertiesOfPeer;

public class GetFileThread extends Thread {
	String fileName;
	TorrentMetaData fileMettaData;
	Status peerStatus;
	ConcurrencyManager concurManager;
	InflightChunkRequestManager icrm;
	Hashtable<String, Status> listOfOtherPeersStatus;
	
	public GetFileThread(String fileName, TorrentMetaData fileMettaData) {
		this.fileName = fileName;
		this.fileMettaData = fileMettaData;
		this.peerStatus = PropertiesOfPeer.getCurrentPeerStatus();
		this.concurManager = PropertiesOfPeer.peerConcurrencyManager;
		this.icrm = new InflightChunkRequestManager(fileMettaData.getNumberOfChunks());
		this.listOfOtherPeersStatus = PropertiesOfPeer.listOfOtherPeersStatus;
	}

	@Override
	public void run() {
		try {
			Hashtable<String, Integer[]> fileNameChunkReplicationMap = peerStatus.fileNameChunkReplicationMap;
			Integer[] fileReplicationArray = (Integer[]) fileNameChunkReplicationMap.get(fileName);
			this.listOfOtherPeersStatus = PropertiesOfPeer.listOfOtherPeersStatus;
			
			while (true) {
				// Keep running this until file is complete
				
				//Check local chunks, and pass whatever he said to himself
				//Determine which chunk index am I going to check
				//Ask Pinto if we need this chunk
				//Ask Pinto if this chunk is already in flight
				//Tell Pinto chunk request sent right after
				
				ArrayList<Integer> previousIndexes = new ArrayList<Integer>();
				int j = 0;
				while (true) {

					int minValue = Integer.MAX_VALUE;
					int minIndex = 0;
					for (int i = 0; i < fileReplicationArray.length; i++) {
						if (fileReplicationArray[i] < minValue && !previousIndexes.contains(i)) {
							minValue = fileReplicationArray[i];
							minIndex = i;
						}
					}
					previousIndexes.add(minIndex);
					//Call Pinto's function with minIndex
					boolean shouldWeGetThisChunk = icrm.chunkNeeded(minIndex);
					
					if (shouldWeGetThisChunk){
						if (!icrm.isChunkInflight(minIndex)){
							//Find out which host has this chunk
							String hostNameToGetChunk = getHostNameForChunk(fileName, minIndex);

							icrm.noteChunkRequest(minIndex);
							//Send chunk request
							RequestForChunk requestForChunkThread = new RequestForChunk(fileName, hostNameToGetChunk, minIndex);
							requestForChunkThread.start();
						}
					}
					else{
						
					}
				}
			}
		} catch (Exception e) {
			System.err.println("Screwed up on check for new files to get");
			e.printStackTrace();
		}
	}
	
	public String getHostNameForChunk(String fileName, int minIndex){
		String hostName = "Nohostnamefound";
		Map<String, Status> map = listOfOtherPeersStatus;
		Iterator<Map.Entry<String, Status>> it = map.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<String, Status> entry = it.next();
			Status otherPeerStatus = entry.getValue();
			if (otherPeerStatus.containsChunk(fileName + "_chunk_" + minIndex)){
				hostName = entry.getKey();
			}
		}
		
		return hostName;
	}
}