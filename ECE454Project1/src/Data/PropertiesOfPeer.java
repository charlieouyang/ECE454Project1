package Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class PropertiesOfPeer {
	//Global variables for the file server and client to access
	public static String ipAddress = "localhost";
	public static int portNumber = 6000;			//Port number of this host
	public static String PeerName = ipAddress + "." + portNumber;
	public static ArrayList<Entry> ipAddrPortNumMappingAll = new ArrayList<Entry>();
	public static ArrayList<Entry> ipAddrPortNumMappingAlive = new ArrayList<Entry>();
	public static boolean peerUp = true;
	
	public PropertiesOfPeer(){
		//List of ip address to port number mappings
		Map.Entry<String, Integer> entry1 = new MyEntry<String, Integer>("localhost", 7000);
		ipAddrPortNumMappingAll.add(entry1);
		Map.Entry<String, Integer> entry2 = new MyEntry<String, Integer>("localhost", 8000);
		ipAddrPortNumMappingAll.add(entry2);
	}
	
	public static void AddEntryToIPAddrPortNumMappingAlive(String ipAddress, int portNumber){
		Map.Entry<String, Integer> entry = new MyEntry<String, Integer>(ipAddress, portNumber);
		ipAddrPortNumMappingAlive.add(entry);
	}
	
	public static void RemoveEntryFromIPAddrPortNumMappingAlive(String receivedIpAddress, int receivedPortNumber){
		//Map.Entry<String, Integer> entry = new MyEntry<String, Integer>(ipAddress, portNumber);
		//ipAddrPortNumMappingAlive.remove(entry);
		
		Iterator<Entry> it = ipAddrPortNumMappingAlive.iterator();
		while (it.hasNext()) {
			Entry entry = it.next();
			if (entry.getKey().equals(receivedIpAddress)
					&& entry.getValue().equals(receivedPortNumber)) {
				it.remove();
			}
		}
	}
	
	public static void ShutDownThisPeer(){
		peerUp = false;

		Iterator<Entry> it = ipAddrPortNumMappingAlive.iterator();
		while (it.hasNext()) {
			Entry entry = it.next();
			it.remove();
		}
	}
	
	public static void StartThisPeer(){
		peerUp = true;
	}
}
