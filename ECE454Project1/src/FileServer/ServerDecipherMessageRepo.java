package FileServer;

import java.awt.TrayIcon.MessageType;

import Data.Message;
import Data.PropertiesOfPeer;

public class ServerDecipherMessageRepo {
	
	public static Message DecipherMessageAndReturn(Message incomingMessage) {
		
		Message.MESSAGE_TYPE type = incomingMessage.getType();
		Message returnMessage = null;
		
		System.out.println("got message and trying to decipher");
		
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
			//Call the chunk manager to send chunk
			//No need for another thread since FileServerThreadWorkDispatcher will send the return message
			returnMessage = ReturnChunkFromChunkRequest(incomingMessage);
		}
		else if (type.equals(Message.MESSAGE_TYPE.CHUNK_RESPONSE)){
			//Do some chunk operations to update this peer
			returnMessage = ReceiveChunkResponse(incomingMessage);
		}
		
		//File and chunk list management messages
		else if (type.equals(Message.MESSAGE_TYPE.STATUS_REQUEST)){
			//Call the file manager/chunk manager to return list of files and chunks
			//No need for another thread since FileServerThreadWorkDispatcher will send the return message
			returnMessage = ReturnStatusFromStatusRequest(incomingMessage);
		}
		else if (type.equals(Message.MESSAGE_TYPE.STATUS_UPDATE_RESPONSE)){
			//Do some status operations to update this peer
			returnMessage = ReceiveStatusResponse(incomingMessage);
		}
		
		else{
			System.err.println("Unknown message type... You fucked up!");
		}
		
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
	
	public static Message ReturnChunkFromChunkRequest(Message chunkRequestMessage){
		// Call Pinto's method to get a chunk based on the chunkRequestMessage
		// and return the requested chunk
		// Pinto'sMethod(chunkRequestMessage)
		Message returnChunkMessage = new Message("", 0, null, null);
		return returnChunkMessage;
	}
	
	public static Message ReturnStatusFromStatusRequest(Message statusRequestMessage){
		// Call Pinto's method to get a status based on the peer
		// and return the requested status
		// Pinto'sMethod()
		Message returnStatusMesssage = new Message("", 0, null, null);
		return returnStatusMesssage;
	}
	
	public static Message ReceiveChunkResponse(Message chunkResponseMessage){
		//Update the current chunk request process
		return null;
	}
	
	public static Message ReceiveStatusResponse(Message statusResponseMessage){
		//Update the status object of this peer based on other peer statuses
		return null;
	}
}
