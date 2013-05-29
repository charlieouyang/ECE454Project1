package Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PropertiesOfPeer {
	//Global variables for the file server and client to access
	public static int PeerID = 1;
	public static String ipAddress = "localhost";
	public static int portNumber = 7000;			//Port number of this host
	public static ArrayList<Entry> ipAddrPortNumMappingAll = new ArrayList<Entry>();
	public static ArrayList<Entry> ipAddrPortNumMappingAlive = new ArrayList<Entry>();
	
	public PropertiesOfPeer(){
		//List of ip address to port number mappings
		Map.Entry<String, Integer> entry1 = new MyEntry<String, Integer>("localhost", 6000);
		ipAddrPortNumMappingAll.add(entry1);
	}
	
	public static void AddEntryToIPAddrPortNumMappingAlive(String ipAddress, int portNumber){
		Map.Entry<String, Integer> entry1 = new MyEntry<String, Integer>(ipAddress, portNumber);
		ipAddrPortNumMappingAlive.add(entry1);
	}
}
