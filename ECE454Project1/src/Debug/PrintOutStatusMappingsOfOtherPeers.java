package Debug;

import java.util.Iterator;
import java.util.Map;

import justen.Status;
import Data.PropertiesOfPeer;

public class PrintOutStatusMappingsOfOtherPeers extends Thread {
	
	public PrintOutStatusMappingsOfOtherPeers() {
	}

	@Override
	public void run() {
		while (true) {
			System.out.println("Printing out status list for other peers");
			System.out.println("This peer: " + PropertiesOfPeer.getCurrentPeerStatus().numFiles);
			
			Iterator<Map.Entry<String, Status>> it = PropertiesOfPeer.listOfOtherPeersStatus.entrySet().iterator();

			while (it.hasNext()) {
				Map.Entry<String, Status> entry = it.next();

				// Remove entry if key is null or equals 0.
				if (entry.getKey() != null || entry.getKey() != null) {
					Status tempStatus = entry.getValue();
					System.out.println("start");
					System.out.println(entry.getKey() + ":" + tempStatus.numFiles);
					System.out.println("end");
				}
			}
			
			try {
			    Thread.sleep(1000);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		}
	}
}
