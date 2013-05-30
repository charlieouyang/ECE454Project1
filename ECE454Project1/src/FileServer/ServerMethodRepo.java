package FileServer;

import Data.Message;
import Data.PropertiesOfPeer;

public class ServerMethodRepo {
	
	public static Message DecipherMessageAndReturn(Message incomingMessage) {
		String intention = incomingMessage.getIntention();
		Message returnMessage = null;
		
		if (intention.equals("Are you alive?")){
			returnMessage = RespondToAnotherPeerBroadCastMessage(incomingMessage);
		}
		else if (intention.equals("Yes I am alive!")){
			returnMessage = ReceiveBroadcastStatusFromPeer(incomingMessage);
		}
		else if (intention.equals("Closing connection!")){
			returnMessage = ReceiveClosingConnectionFromPeer(incomingMessage);
		}
		
		System.out.println("got message and trying to decipher");
		return returnMessage;
	}
	
	public static Message RespondToAnotherPeerBroadCastMessage(Message broadcastMessage){
		Message returnMessage = new Message(PropertiesOfPeer.ipAddress, PropertiesOfPeer.portNumber, "Yes I am alive!");
		return returnMessage;
	}
	
	public static Message ReceiveBroadcastStatusFromPeer(Message broadcastReturn){
		String ipAddress = broadcastReturn.getIpAddress();
		int portNumber = broadcastReturn.getPortNumber();
		PropertiesOfPeer.AddEntryToIPAddrPortNumMappingAlive(ipAddress, portNumber);
		return null;
	}
	
	public static Message GetClosingConnectionMessage(){
		String ipAddress = PropertiesOfPeer.ipAddress;
		int portNumber = PropertiesOfPeer.portNumber;
		Message closingMessage = new Message(ipAddress, portNumber, "Closing connection!");
		return closingMessage;
	}
	
	public static Message ReceiveClosingConnectionFromPeer(Message closingMessage){
		String ipAddress = closingMessage.getIpAddress();
		int portNumber = closingMessage.getPortNumber();
		PropertiesOfPeer.RemoveEntryFromIPAddrPortNumMappingAlive(ipAddress, portNumber);
		return null;
	}
}
