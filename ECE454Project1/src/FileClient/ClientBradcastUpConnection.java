package FileClient;

import java.io.ObjectOutputStream;
import java.net.Socket;

import Data.Message;
import Data.PropertiesOfPeer;

//This will take care of each individual connection with another peer
//It will request for a list of files that another peer has

public class ClientBradcastUpConnection extends Thread {
	private Socket socket;
	private String ipAddress;
	private int portNumber;

	public ClientBradcastUpConnection(String ipAddress, int portNumber) throws Exception {
		this.ipAddress = ipAddress;
		this.portNumber = portNumber;
	}

	// Contact the peer and ask for a list of files
	@Override
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
	
				Message pingMessage = new Message(PropertiesOfPeer.ipAddress, PropertiesOfPeer.portNumber, Message.MESSAGE_TYPE.PEER_DISCOVER, null);
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
