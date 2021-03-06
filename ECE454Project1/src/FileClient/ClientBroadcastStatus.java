package FileClient;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

//This class will spawn all of the connection threads that will connect to other hosts

public class ClientBroadcastStatus extends Thread {
	private ArrayList<Entry> ipAddrPortNumMapping;
	
	public ClientBroadcastStatus(ArrayList<Entry> ipAddrPortNumMapping) {
		this.ipAddrPortNumMapping = ipAddrPortNumMapping;
	}
	
	@Override
	public void run() {
		try {
			//Do a broadcast for Peer status		    
		    for (Map.Entry<String, Integer> entry : ipAddrPortNumMapping) {
		    	String ipAddress = entry.getKey();
		        int portNumber = entry.getValue();
		        
		        ClientBroadcastStatusConnection clientConnectionThread = new ClientBroadcastStatusConnection(ipAddress, portNumber);
		        clientConnectionThread.start();
		    }
		} catch (Exception e) {
			System.err.println("Screwed up on FileBroadcastStatus");
			e.printStackTrace();
		}
	}

}
