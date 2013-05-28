package Data;

import java.io.Serializable;

public class Message implements Serializable {
    private String ipAddress;		//Of sender
    private int portNumber;			//Of sender
    private String messageText;

    public Message(String ipAddress, int portNumber, String text) {
    	this.ipAddress = ipAddress;
    	this.portNumber = portNumber;
        this.messageText = text;
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
