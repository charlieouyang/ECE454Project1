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
			
		} catch (Exception e) {
			System.err.println("Screwed up on check for new files to get");
			e.printStackTrace();
		}
	}
}