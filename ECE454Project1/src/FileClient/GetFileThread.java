package FileClient;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import justen.ConcurrencyManager;
import justen.DirectoryHelper;
import justen.HelperClass;
import justen.InflightChunkRequestManager;
import justen.Status;
import justen.TorrentMetaData;
import Data.PropertiesOfPeer;

public class GetFileThread extends Thread {
	String fileName;
	TorrentMetaData fileMetaData;
	Status peerStatus;
	ConcurrencyManager concurManager;
	InflightChunkRequestManager icrm;
	Hashtable<String, Status> listOfOtherPeersStatus;
	int writeCount;
	
	public GetFileThread(String fileName, TorrentMetaData fileMettaData) {
		this.fileName = fileName;
		this.fileMetaData = fileMettaData;
		this.peerStatus = PropertiesOfPeer.getCurrentPeerStatus();
		this.concurManager = PropertiesOfPeer.peerConcurrencyManager;
		this.icrm = new InflightChunkRequestManager(fileMettaData.getNumberOfChunks());
		this.listOfOtherPeersStatus = PropertiesOfPeer.listOfOtherPeersStatus;
		writeCount = 0;
	}

	@Override
	public void run() {
		try {
			Integer[] fileReplicationArray = null;
			// Keep running this until file is complete
			
			//Check local chunks, and pass whatever he said to himself
			//Determine which chunk index am I going to check
			//Ask Pinto if we need this chunk
			//Ask Pinto if this chunk is already in flight
			//Tell Pinto chunk request sent right after
			
			int j = 0;
			ArrayList<Integer> previousIndexes = new ArrayList<Integer>();
			while (PropertiesOfPeer.peerUp) {
				
				String aggList = concurManager.getAggregateChunks(fileName);
				ArrayList<Integer> list = HelperClass.getArrayList(aggList);
				icrm.updateReceivedChunks(list);

				if (icrm.isFileComplete()) {

					// now that file is completely built, must delete the chunks\fileName directory
					DirectoryHelper.deleteFolder(new File(concurManager.getChunkPath() + "/" + fileName));
					break;
				}
				
				this.listOfOtherPeersStatus = PropertiesOfPeer.listOfOtherPeersStatus;
				for (Entry<String, Status> e : listOfOtherPeersStatus.entrySet()) {
					if (e.getValue().fileNameChunkReplicationMap.containsKey(fileName)) {
						fileReplicationArray = new Integer[fileMetaData.getNumberOfChunks()];
					}
				}
				for (int i = 0; i < fileReplicationArray.length; i++)
					fileReplicationArray[i] = 0;
				
				
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
						writeCount++;
						if (writeCount > 9) {
							PropertiesOfPeer.updateCurrentPeerStatus();
							PropertiesOfPeer.broadcastStatus();
							writeCount = 0;
						}
					}
				}
				else{
					
				}
			}
			System.out.println("[** SYSTEM NOTIFICATION **]	Finished getting file.");
			PropertiesOfPeer.listOfFilesToGet.remove(fileName);
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
