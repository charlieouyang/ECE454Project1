package Data;

import java.io.Serializable;

public class Message implements Serializable {
	
	public enum MESSAGE_TYPE {
		PEER_DISCOVER, 				// BROADCAST
		ACK_PEER_DISCOVER,
		PEER_LEAVING, 				// BROADCAST
		
		CHUNK_REQUEST,
		CHUNK_RESPONSE,
		
		STATUS_REQUEST,
		STATUS_UPDATE_RESPONSE
	}
	
    private String ipAddress;		//Of sender
    private int portNumber;			//Of sender
    private MESSAGE_TYPE msgType;
    private Object data;
    
    public Message(String ipAddress, int portNumber, MESSAGE_TYPE type, Object data) {
    	this.ipAddress = ipAddress;
    	this.portNumber = portNumber;
    	this.msgType = type;
    	this.data = data;
    }
    
    public MESSAGE_TYPE getType() {
        return this.msgType;
    }
    
    public String getIpAddress(){
    	return ipAddress;
    }
    
    public int getPortNumber(){
    	return portNumber;
    }
    
    public Object getData(){
    	return this.data;
    }
}
