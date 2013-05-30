package FileClient;

import java.util.Map.Entry;

import Data.PropertiesOfPeer;

public class CloseThisConnectionThread extends Thread{
	public CloseThisConnectionThread() {
	}

	public void run() {
		//Instantiate the thread for leaving network
		ClientCloseThisBroadcast closeConnectionThread = new ClientCloseThisBroadcast(PropertiesOfPeer.ipAddrPortNumMappingAlive);
		closeConnectionThread.start();
	}
}
