package Data;

import java.io.Serializable;

public class Message implements Serializable {
	
	public enum MESSAGE_TYPE {
		ANNOUNCE, // BROADCAST
		CHUNK_REQUEST,
		CHUNK_RESPONSE,
		CHUNK_UNAVAILABLE,
		FILE_LIST_REQUEST, // BROADCAST
		FILE_LIST_RESPONSE, // BROADCAST
		FILE_LIST_UNAVAILABLE, // BROADCAST
		FILE_CHUNK_LIST_REQUEST, // BROADCAST
		LEAVE // BROADCAST
	}
	
    private String ipAddress;		//Of sender
    private int portNumber;			//Of sender
    private String messageText;
    private MESSAGE_TYPE msgType;
    private Object data;

    public Message(String ipAddress, int portNumber, String text) {
    	this.ipAddress = ipAddress;
    	this.portNumber = portNumber;
        this.messageText = text;
    }
    
    public Message(String ipAddress, int portNumber, MESSAGE_TYPE type, Object data) {
    	this.ipAddress = ipAddress;
    	this.portNumber = portNumber;
    	this.msgType = type;
    	this.data = data;
    }
    
    public String getIntention() {
        return messageText;
    }
    
    public String getIpAddress(){
    	return ipAddress;
    }
    
    public int getPortNumber(){
    	return portNumber;
    }
}
