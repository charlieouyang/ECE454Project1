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

import Data.PropertiesOfPeer;

//This class will spawn all of the connection threads that will connect to other hosts

public class ClientCloseThisBroadcast extends Thread {
	private ArrayList<Entry> ipAddrPortNumMapping;
	
	public ClientCloseThisBroadcast(ArrayList<Entry> ipAddrPortNumMapping) {
		this.ipAddrPortNumMapping = ipAddrPortNumMapping;
	}
	
	@Override
	public void run() {
		try {
			//Do a broadcast for Peer status		    
		    for (Map.Entry<String, Integer> entry : ipAddrPortNumMapping) {
		    	String ipAddress = entry.getKey();
		        int portNumber = entry.getValue();
		        
		        ClientCloseThisBroadcastConnection clientCloseConnectionThread = new ClientCloseThisBroadcastConnection(ipAddress, portNumber);
		        clientCloseConnectionThread.start();
		    }
		    
		    System.out.println("Shutting down this server " + PropertiesOfPeer.ipAddress + " " + PropertiesOfPeer.portNumber);
		    PropertiesOfPeer.ShutDownThisPeer();
		    
		} catch (Exception e) {
			System.err.println("Screwed up on FileClient");
			e.printStackTrace();
		}
	}

}
