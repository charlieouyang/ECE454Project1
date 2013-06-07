package FileClient;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Data.ChunkRequest;
import Data.Message;
import Data.Message.MESSAGE_TYPE;
import Data.PropertiesOfPeer;

public class RequestForChunk extends Thread {
	private Socket socket = null;
	private String destIpAddress;
	private int destPortNumber;
	private String chunkName;
	private int chunkNumber;

    public RequestForChunk(String chunkName, String hostName, int chunkNumber) {
    	this.destIpAddress = GetIpAddressFromName(hostName);
    	this.destPortNumber = GetPortNumberFromName(hostName);
    	this.chunkName = chunkName;
    	this.chunkNumber = chunkNumber;
    }

    //This thread will do 2 things
    //1) Sending out list of files when requested
    //2) Sending requested files
    @Override
	public void run() {

		try {
		    //System.out.println("Sending chunk request " + chunkName + chunkNumber + " to " + destPortNumber);
			
			Socket socket = new Socket(destIpAddress, destPortNumber);
			//Send to other peer response
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());			

			ChunkRequest chunkRequest = new ChunkRequest(chunkName, chunkNumber);
			
			Message requestChunkMessage = new Message(PropertiesOfPeer.ipAddress, PropertiesOfPeer.portNumber, MESSAGE_TYPE.CHUNK_REQUEST, chunkRequest);
			
			objectOutputStream.writeObject(requestChunkMessage);

			//System.out.println("Closing the connection for sending gracefully");
			
			//objectOutputStream.close();
			objectOutputStream.close();
			
		    socket.close();
	
		} catch (IOException e) {
		    e.printStackTrace();
		} 
    }
    
    public String GetIpAddressFromName(String hostName) {
		int lastDotIndex = hostName.lastIndexOf(".");
		return hostName.substring(0, lastDotIndex);
	}

	public int GetPortNumberFromName(String hostName) {
		int lastDotIndex = hostName.lastIndexOf(".");
		String portNumber = hostName.substring(lastDotIndex + 1, hostName.length());
		return Integer.parseInt(portNumber);
	}
}
