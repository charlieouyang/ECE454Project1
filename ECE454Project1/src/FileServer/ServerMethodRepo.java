package FileServer;

import java.awt.TrayIcon.MessageType;

import Data.Message;
import Data.PropertiesOfPeer;

public class ServerMethodRepo {
	
	public static Message DecipherMessageAndReturn(Message incomingMessage) {
		
		Message.MESSAGE_TYPE type = incomingMessage.getType();
		Message returnMessage = null;
		
		//Peer connection establishment management messages
		if (type.equals(Message.MESSAGE_TYPE.PEER_DISCOVER)){
			returnMessage = RespondToAnotherPeerBroadCastMessage(incomingMessage);
		}
		else if (type.equals(Message.MESSAGE_TYPE.ACK_PEER_DISCOVER)){
			returnMessage = ReceiveBroadcastStatusFromPeer(incomingMessage);
		}
		else if (type.equals(Message.MESSAGE_TYPE.PEER_LEAVING)){
			returnMessage = ReceiveClosingConnectionFromPeer(incomingMessage);
		}
		
		//Chunk send and receive messages
		else if (type.equals(Message.MESSAGE_TYPE.CHUNK_REQUEST)){
			
		}
		else if (type.equals(Message.MESSAGE_TYPE.CHUNK_RESPONSE)){
			
		}
		else if (type.equals(Message.MESSAGE_TYPE.CHUNK_UNAVAILABLE)){
			
		}
		
		//File and chunk list management messages
		else if (type.equals(Message.MESSAGE_TYPE.FILE_LIST_REQUEST)){
			
		}
		else if (type.equals(Message.MESSAGE_TYPE.FILE_LIST_RESPONSE)){
			
		}
		else if (type.equals(Message.MESSAGE_TYPE.CHUNK_LIST_REQUEST)){
			
		}
		else if (type.equals(Message.MESSAGE_TYPE.CHUNK_LIST_RESPONSE)){
			
		}
		else if (type.equals(Message.MESSAGE_TYPE.FILE_LIST_UNAVAILABLE)){
			
		}
		
		else{
			System.err.println("Unknown message type... You fucked up!");
		}
		
		System.out.println("got message and trying to decipher");
		return returnMessage;
	}
	
	public static Message RespondToAnotherPeerBroadCastMessage(Message broadcastMessage){
		Message returnMessage = new Message(PropertiesOfPeer.ipAddress, PropertiesOfPeer.portNumber, Message.MESSAGE_TYPE.ACK_PEER_DISCOVER, null);
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
		Message closingMessage = new Message(ipAddress, portNumber, Message.MESSAGE_TYPE.PEER_LEAVING, null);
		return closingMessage;
	}
	
	public static Message ReceiveClosingConnectionFromPeer(Message closingMessage){
		String ipAddress = closingMessage.getIpAddress();
		int portNumber = closingMessage.getPortNumber();
		PropertiesOfPeer.RemoveEntryFromIPAddrPortNumMappingAlive(ipAddress, portNumber);
		return null;
	}
}
