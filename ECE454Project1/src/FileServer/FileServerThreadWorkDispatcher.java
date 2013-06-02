package FileServer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import Data.Message;

public class FileServerThreadWorkDispatcher extends Thread {
	private Socket socket = null;

    public FileServerThreadWorkDispatcher(Socket socket) {
		this.socket = socket;
    }

    //This thread will do 2 things
    //1) Sending out list of files when requested
    //2) Sending requested files
    @Override
	public void run() {

		try {
		    
			//Take client input and see what they want
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			//ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());			

			int tracker = 1;

			System.out.println("Trying: " + tracker);

			Message incomingMessage = (Message) objectInputStream.readObject();

			// Decipher to see what the client wants
			if (incomingMessage != null) {
				Message returnMessage = ServerDecipherMessageRepo.DecipherMessageAndReturn(incomingMessage);

				if (returnMessage != null) {
					String ipAddress = incomingMessage.getIpAddress();
					int portNumber = incomingMessage.getPortNumber();
					
					//Send back a response
					FileServerThreadSendMessage sendMessageThread = new FileServerThreadSendMessage(ipAddress, portNumber, returnMessage);
					sendMessageThread.start();
					
				} else {
					// No message to return
				}
			}
			else {
				// do nothing
			}
			
			System.out.println("Closing the connection for receiving gracefully");
			
			objectInputStream.close();
			
		    socket.close();
	
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}
    }
    
}
