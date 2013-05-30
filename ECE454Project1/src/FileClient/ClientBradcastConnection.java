package FileClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

import Data.Message;
import Data.PropertiesOfPeer;

//This will take care of each individual connection with another peer
//It will request for a list of files that another peer has

public class ClientBradcastConnection extends Thread {
	private Socket socket;
	private String ipAddress;
	private int portNumber;

	public ClientBradcastConnection(String ipAddress, int portNumber) throws Exception {
		this.ipAddress = ipAddress;
		this.portNumber = portNumber;
	}

	// Contact the peer and ask for a list of files
	public void run() {
		System.out.println("Connecting to: " + ipAddress + ", " + portNumber);
		try {
			boolean stillTryingToConnect = true;
			
			while (stillTryingToConnect) {
				try {
					stillTryingToConnect = PropertiesOfPeer.peerUp;
					socket = new Socket(ipAddress, portNumber);
					if (socket != null) {
						break;
					}
				} catch (Exception e) {
					// handle exceptions
					// possibly add a sleep period
					System.err.println("Can't connect to " + ipAddress + " " + portNumber +
							"... waiting for 5 sec");
					Thread.sleep(5000);
				}
			}
			
			if (stillTryingToConnect == true){
				ObjectOutputStream oos = new ObjectOutputStream(
						socket.getOutputStream());
	
				Message pingMessage = new Message(PropertiesOfPeer.ipAddress, PropertiesOfPeer.portNumber, "Are you alive?");
				oos.writeObject(pingMessage);
				
	
				oos.close();
			}
			//socket.close();
			
		} catch (Exception e) {
			System.out.println("Failed on trying to ping one peer");
			e.printStackTrace();
		}
	}

}
