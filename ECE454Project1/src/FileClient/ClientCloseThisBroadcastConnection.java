package FileClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

import Data.Message;
import Data.PropertiesOfPeer;
import FileServer.ServerDecipherMessageRepo;

//This will take care of each individual connection with another peer
//It will request for a list of files that another peer has

public class ClientCloseThisBroadcastConnection extends Thread {
	private Socket socket;
	private String ipAddress;
	private int portNumber;

	public ClientCloseThisBroadcastConnection(String ipAddress, int portNumber) throws Exception {
		this.ipAddress = ipAddress;
		this.portNumber = portNumber;
	}

	// Contact the peer and ask for a list of files
	public void run() {
		System.out.println("Connecting to: " + ipAddress + ", " + portNumber + " and inform of shutdown");
		try {
			while (true) {
				try {
					socket = new Socket(ipAddress, portNumber);
					if (socket != null) {
						break;
					}
				} catch (Exception e) {
					// handle exceptions
					// possibly add a sleep period
					System.err.println("Closing connection screwed up... trying again later");
					Thread.sleep(5000);
				}
			}

			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());

			Message pingMessage = ServerDecipherMessageRepo.GetClosingConnectionMessage();
			oos.writeObject(pingMessage);

			oos.close();
			//socket.close();
			
		} catch (Exception e) {
			System.out.println("Failed on trying to ping one peer");
			e.printStackTrace();
		}
	}

}
