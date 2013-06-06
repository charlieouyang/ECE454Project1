package UserInput;

import java.util.Scanner;

import justen.Status;

import Data.PropertiesOfPeer;
import FileClient.CloseThisConnectionThread;

public class UserInputThread extends Thread {
	public UserInputThread() {
	}

	@Override
	public void run() {
		try {
			while (true){
				Scanner scanner = new Scanner (System.in);
				System.out.print("Please enter operation");  
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
						// Shutdown the server!
						
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
					PropertiesOfPeer.broadcastStatus();
					//Refresh and broadcast status
					System.out.println("Inserted file and broadcasted new status");
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