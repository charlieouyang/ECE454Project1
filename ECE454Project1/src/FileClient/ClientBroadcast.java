package FileClient;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.*;

//This class will spawn all of the connection threads that will connect to other hosts

public class ClientBroadcast extends Thread {
	private ArrayList<Entry> ipAddrPortNumMapping;
	
	public ClientBroadcast(ArrayList<Entry> ipAddrPortNumMapping) {
		this.ipAddrPortNumMapping = ipAddrPortNumMapping;
	}
	
	@Override
	public void run() {
		try {
			//Do a broadcast for Peer status		    
		    for (Map.Entry<String, Integer> entry : ipAddrPortNumMapping) {
		    	String ipAddress = entry.getKey();
		        int portNumber = entry.getValue();
		        
		        ClientBradcastConnection clientConnectionThread = new ClientBradcastConnection(ipAddress, portNumber);
		        clientConnectionThread.start();
		    }
		    
		    System.out.println("Done?");
		    
		    			
		} catch (Exception e) {
			System.err.println("Screwed up on FileClient");
			e.printStackTrace();
		}
	}

}
