package FileServer;
import java.net.*;
import java.io.*;

import Data.Message;

public class FileServerConnection extends Thread {
	private Socket socket = null;

    public FileServerConnection(Socket socket) {
		this.socket = socket;
    }

    //This thread will do 2 things
    //1) Sending out list of files when requested
    //2) Sending requested files
    public void run() {

		try {
		    
			//Take client input and see what they want
			InputStream inputStream = socket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
						
			Message incomingMessage = (Message) objectInputStream.readObject();
			
			//Decipher to see what the client wants
			if (incomingMessage != null){
				String intention = incomingMessage.getIntention();
			}
			else{
				//Do nothing?
			}
			
			
			objectInputStream.close();
			
		    socket.close();
	
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}
    }
}
