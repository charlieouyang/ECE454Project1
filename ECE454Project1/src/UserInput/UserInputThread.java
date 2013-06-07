package UserInput;

import java.util.Scanner;

import justen.Status;

import Data.PropertiesOfPeer;
import FileClient.CheckForNewFileToGet;
import FileClient.ClientBroadcastStatus;
import FileClient.ClientBroadcastUp;
import FileClient.CloseThisConnectionThread;
import FileServer.FileServer;

public class UserInputThread extends Thread {
	public UserInputThread() {
	}

	@Override
	public void run() {
		try {
			while (true){
				Scanner scanner = new Scanner (System.in);
				System.out.println("Please enter operation");  
				String input = scanner.next(); 
				
				if (input.equals("leave")) {
					if (PropertiesOfPeer.peerUp) {
						// Shutdown the server!
						CloseThisConnectionThread closePeerThread = new CloseThisConnectionThread();
						closePeerThread.start();
					} else {
						System.out.println("System is already shutdown");
					}
				}
				else if (input.equals("join")){
					if (!PropertiesOfPeer.peerUp) {
						// Turn the server on
						PropertiesOfPeer.peerUp = true;
						
						FileServer fileServerThread = new FileServer(PropertiesOfPeer.portNumber);
						fileServerThread.start();
						System.out.println("Running file server thread");
						
						// 2) Invoke the File Client Thread		
						ClientBroadcastUp fileClientThread = new ClientBroadcastUp(PropertiesOfPeer.ipAddrPortNumMappingAll);
						fileClientThread.start();
						
						// Broadcast thread for status
						ClientBroadcastStatus statusBroadcastThread = new ClientBroadcastStatus(PropertiesOfPeer.ipAddrPortNumMappingAll);
						statusBroadcastThread.start();	
						
						CheckForNewFileToGet checkForNewFileThread = new CheckForNewFileToGet();
						checkForNewFileThread.start();
						
					} else {
						System.out.println("System is already up");
					}
				}
				else if (input.equals("insert")){
					System.out.println("Please enter full file name");
					String fileName = scanner.next(); 
					
					if (fileName.equals("end")){
						continue;
					}
					PropertiesOfPeer.peerConcurrencyManager.insertFile(fileName);
					PropertiesOfPeer.updateCurrentPeerStatus();
					if (PropertiesOfPeer.peerUp) {
						PropertiesOfPeer.broadcastStatus();
						// Refresh and broadcast status
					}
				}
				else if (input.equals("query")) {
					PropertiesOfPeer.printStatusInformation();
				}
			}
		} catch (Exception e) {
			System.err.println("User console error");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public void InitializeSystem(){
		
	}
}