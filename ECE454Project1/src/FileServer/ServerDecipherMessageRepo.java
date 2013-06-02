package FileServer;

import Data.ChunkRequest;
import Data.ChunkResponse;
import Data.Message;
import Data.Message.MESSAGE_TYPE;
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
			ChunkRequest request = (ChunkRequest)incomingMessage.getData();
			String fileName = request.getFileName();
			int chunkNum = request.getChunkNumber();
			byte[] chunk = PropertiesOfPeer.peerConcurrencyManager.getChunkData(fileName, chunkNum);
			ChunkResponse chunkResponse = new ChunkResponse(fileName, chunkNum, chunk);
			
			returnMessage = new Message(incomingMessage.getIpAddress(), incomingMessage.getPortNumber(), MESSAGE_TYPE.CHUNK_RESPONSE, chunkResponse);
		}
		else if (type.equals(Message.MESSAGE_TYPE.CHUNK_RESPONSE)){
			//Do some chunk operations to update this peer
			returnMessage = ReceiveChunkResponse(incomingMessage);
		}
		
		//File and chunk list management messages
		else if (type.equals(Message.MESSAGE_TYPE.STATUS_UPDATE)){
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
		PropertiesOfPeer.updateOtherPeersStatus(statusResponseMessage);
		return null;
	}
}
