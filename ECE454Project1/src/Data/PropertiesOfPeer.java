package Data;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import justen.ConcurrencyManager;
import justen.Status;

public class PropertiesOfPeer {
	//Global variables for the file server and client to access
	public static String ipAddress = "localhost";
	public static int portNumber = 6000;			//Port number of this host
	public static String PeerName = ipAddress + "." + portNumber;
	public static ArrayList<Entry> ipAddrPortNumMappingAll = new ArrayList<Entry>();
	public static ArrayList<Entry> ipAddrPortNumMappingAlive = new ArrayList<Entry>();
	public static boolean peerUp = true;
	
	//File management and synchronization stuff
	public static ConcurrencyManager peerConcurrencyManager;
	public static Status currentPeerStatus;
	public static Hashtable<String, Status> listOfOtherPeersStatus;		//PeerName mapped to status
	ArrayList<String> listOfCompleteFiles;
	ArrayList<String> listOfIncompleteFiles;
	ArrayList<String> listOfChunks;
	
	public PropertiesOfPeer(){
		//Stuff to keep track of files/chunks and other peers
		peerConcurrencyManager = new ConcurrencyManager(PeerName);
		currentPeerStatus = new Status(peerConcurrencyManager);
		listOfOtherPeersStatus = new Hashtable<String, Status>();
		
		listOfCompleteFiles = new ArrayList<String>();
		listOfIncompleteFiles = new ArrayList<String>();
		listOfChunks = new ArrayList<String>();
		
		//List of ip address to port number mappings
		Map.Entry<String, Integer> entry1 = new MyEntry<String, Integer>("localhost", 7000);
		ipAddrPortNumMappingAll.add(entry1);
		Map.Entry<String, Integer> entry2 = new MyEntry<String, Integer>("localhost", 8000);
		ipAddrPortNumMappingAll.add(entry2);
	}
	
	//This will be used to update the peer's status info
	public static void updateCurrentPeerStatus(){
		currentPeerStatus = new Status(peerConcurrencyManager);
	}
	
	public static synchronized Status getCurrentPeerStatus(){
		Status tempStatus = currentPeerStatus;
		updateCurrentPeerStatus();
		return tempStatus;
	}
	
	//Receive status from another peer and update that info
	public static void updateOtherPeersStatus(Message incomingMessageStatusFromAnotherPeer){
		//Gotta make sure the data object is actually of the status class
		Status anotherPeerStatus = null;
		Object statusData = incomingMessageStatusFromAnotherPeer.getData();
		String senderName = incomingMessageStatusFromAnotherPeer.getSenderName();
		
		//Don't know if the class comparison thing is correct
		if (statusData != null && statusData instanceof Status){
			anotherPeerStatus = (Status) incomingMessageStatusFromAnotherPeer.getData();
			
			System.out.println("This is what you want!!!: " + anotherPeerStatus.numberOfFiles());
			
			listOfOtherPeersStatus.put(senderName, anotherPeerStatus);
		}
		else{
			//Don't do anything because it's an unknown object...
			System.err.println("It's not a status object...");
		}
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
