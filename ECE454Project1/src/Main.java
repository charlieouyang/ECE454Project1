import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import UserInput.UserInputThread;

import FileClient.ClientBroadcast;
import FileClient.ClientCloseThisBroadcast;
import FileClient.CloseThisConnectionThread;
import FileServer.FileServer;
import Data.*;
import Debug.PrintOutIpPortAliveMapThread;

public class Main {
	
	public static void main(String [ ] args)
	{
		final PropertiesOfPeer properties = new PropertiesOfPeer();
		//Main execution point
		
		//Insert() - This peer has a "temp" list of the files that it has. Insert() will
		//take a file name, and insert the file to its local directory (memory?)
		
		//Query() - query the state of the files on this local peer. It returns
		//information about 
		//	fraction of the file that is available locally
		//	fraction of the file that is available in the system
		//	least replication level
		//	weighted least-replication level
		
		//Join() - pass in a list of peers that this host will join. This
		//will invoke the File Client Thread as it is the one that connects to other peers
		
		//Leave() - gracefully close the connections for the server and client
		//after all jobs are completed
		
		//1) Invoke the File Server Thread
			//Pass in an int for the port number used for the socket
		
		UserInputThread userInputThread = new UserInputThread();
		userInputThread.start();
		
		FileServer fileServerThread = new FileServer(properties.portNumber);
		fileServerThread.start();
		System.out.println("Running file server thread");
		
		try {
		    Thread.sleep(5000);			//Wait 5 seconds and get all of the peers up
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}

		// 2) Invoke the File Client Thread		
		ClientBroadcast fileClientThread = new ClientBroadcast(properties.ipAddrPortNumMappingAll);
		fileClientThread.start();
		System.out.println("Running file client thread");
		
		PrintOutIpPortAliveMapThread debugThread = new PrintOutIpPortAliveMapThread();
		debugThread.start();
		
		//Shutdown the server!
		//CloseThisConnectionThread closePeerThread = new CloseThisConnectionThread();
		//closePeerThread.start();
		
		//To Do:
		
		//FileServer
		//Add a protocol class to handle the messages (request for new file, file list, chunks, etc etc.)
		
		//File Client
		//Implement a hashmap<string, int> for IP address and port number
		//Add a protocol to handle a list of other host names (IPs) and list of port numbers
		//Problem: Synchronization issues (server isn't up yet and client thread is already trying to connect)
	}

}
