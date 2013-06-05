package FileClient;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import Data.PropertiesOfPeer;

import justen.DirectoryHelper;
import justen.TorrentMetaData;

public class CheckForNewFileToGet extends Thread {
	
	public CheckForNewFileToGet() {
	}

	@Override
	public void run() {
		try {
			// Keep checking for new files to get
			while (true){
				
				if (PropertiesOfPeer.listOfFilesToGet.size() > 0) {
					// Add new file entry
					Iterator<Map.Entry<String, TorrentMetaData>> otherPeersMetaData = PropertiesOfPeer.listOfFilesToGet.entrySet().iterator();
					
					while (otherPeersMetaData.hasNext()) {
						Map.Entry<String, TorrentMetaData> entry = otherPeersMetaData.next();

						if (!PropertiesOfPeer.fileListAlreadyGetting.contains(entry.getKey())) {
							
							String tempStr = entry.getKey();
							TorrentMetaData tempData = entry.getValue();
							
							PropertiesOfPeer.inProcessOfGettingChunks.put(entry.getKey(), entry.getValue());
							PropertiesOfPeer.fileListAlreadyGetting.add(entry.getKey());
						}
					}

					// Start getting the file
					Iterator<Map.Entry<String, TorrentMetaData>> fileNameAndMetaDataToGetList = PropertiesOfPeer.inProcessOfGettingChunks.entrySet().iterator();
					while (fileNameAndMetaDataToGetList.hasNext()) {
						Map.Entry<String, TorrentMetaData> entry = fileNameAndMetaDataToGetList.next();
						
						DirectoryHelper.createDirectory(PropertiesOfPeer.PeerName, entry.getKey());
						
						GetFileThread getFileThread = new GetFileThread(entry.getKey(), entry.getValue());
						getFileThread.start();

						fileNameAndMetaDataToGetList.remove();
					}
				}

			}
		} catch (Exception e) {
			System.err.println("Screwed up on check for new files to get");
			e.printStackTrace();
		}
	}
}