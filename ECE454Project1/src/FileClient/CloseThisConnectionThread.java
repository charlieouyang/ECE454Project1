package FileClient;

import Data.PropertiesOfPeer;

public class CloseThisConnectionThread extends Thread{
	public CloseThisConnectionThread() {
	}

	@Override
	public void run() {
		//Instantiate the thread for leaving network
		ClientCloseThisBroadcast closeConnectionThread = new ClientCloseThisBroadcast(PropertiesOfPeer.ipAddrPortNumMappingAlive);
		closeConnectionThread.start();
	}
}
