package Debug;

import java.io.IOException;
import java.net.*;
import java.util.Map.Entry;

import Data.PropertiesOfPeer;

public class PrintOutIpPortAliveMapThread extends Thread {

	public PrintOutIpPortAliveMapThread() {
	}

	public void run() {
		while (true) {
			System.out.println("Printing out alive list for peer: " + PropertiesOfPeer.portNumber);
			for (Entry entry : PropertiesOfPeer.ipAddrPortNumMappingAlive) {
				System.out.println(entry.getKey() + ":" + entry.getValue());
			}
			
			try {
			    Thread.sleep(5000);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		}
	}
}
