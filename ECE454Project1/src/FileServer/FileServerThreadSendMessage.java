package FileServer;
import java.net.*;
import java.io.*;

import Data.Message;

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
    public void run() {

		try {
		    
			Socket socket = new Socket(destIpAddress, destPortNumber);
			//Send to other peer response
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());			

			objectOutputStream.writeObject(returnMessage);

			System.out.println("Closing the connection for sending gracefully");
			
			//objectOutputStream.close();
			objectOutputStream.close();
			
		    socket.close();
	
		} catch (IOException e) {
		    e.printStackTrace();
		} 
    }
    
}
