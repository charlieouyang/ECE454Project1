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
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());			
			
			while (true){
				Message incomingMessage = (Message) objectInputStream.readObject();
				
				//Decipher to see what the client wants
				if (incomingMessage != null && !incomingMessage.getIntention().equals("Close connection!")){
					Message returnMessage = DecipherMessageAndReturn(incomingMessage);
					objectOutputStream.writeObject(returnMessage);
				}
				else if (incomingMessage.getIntention().equals("Close connection!")){
					break;
				}
				else{
					//do nothing
				}
			}
			
			System.out.println("Closing the connection gracefully");
			
			objectOutputStream.close();
			objectInputStream.close();
			
		    socket.close();
	
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}
    }
    
    private Message DecipherMessageAndReturn(Message incomingMessage){
    	String intention = incomingMessage.getIntention();
    	
    	System.out.println("got message and trying to decipher");
    	Message returnMessage = new Message(69, intention + " returning your call");
    	return returnMessage;
    }
}
