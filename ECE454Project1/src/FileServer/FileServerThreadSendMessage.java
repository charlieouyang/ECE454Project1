package FileServer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Data.Message;
import Data.PropertiesOfPeer;

public class FileServerThreadSendMessage extends Thread {
	private Socket socket = null;
	private String destIpAddress;
	private int destPortNumber;
	private Message returnMessage;

    public FileServerThreadSendMessage(String destIpAddress, int destPortNumber, Message returnMessage) {
    	this.destIpAddress = destIpAddress;
    	this.destPortNumber = destPortNumber;
    	this.returnMessage = returnMessage;
    }

    //This thread will do 2 things
    //1) Sending out list of files when requested
    //2) Sending requested files
    @Override
	public void run() {

		try {
		    
			boolean stillTryingToConnect = true;

			while (stillTryingToConnect) {
				try {
					stillTryingToConnect = PropertiesOfPeer.peerUp;
					socket = new Socket(destIpAddress, destPortNumber);
					if (socket != null) {
						break;
					}
				} catch (Exception e) {
					// handle exceptions
					// possibly add a sleep period
					// System.err.println("Can't connect to " + ipAddress + " "
					// + portNumber +
					// "... waiting for 5 sec");
					Thread.sleep(5000);
				}
			}

			if (stillTryingToConnect == true) {
				//Send to other peer response
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());			

				objectOutputStream.writeObject(returnMessage);

				//System.out.println("Closing the connection for sending gracefully");
				
				//objectOutputStream.close();
				objectOutputStream.close();
				
			    socket.close();
			}
	
		} catch (Exception e) {
		    e.printStackTrace();
		} 
    }
    
}
