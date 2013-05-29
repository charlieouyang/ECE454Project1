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
}
