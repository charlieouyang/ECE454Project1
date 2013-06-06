package FileServer;

import java.io.IOException;
import java.net.ServerSocket;

import Data.PropertiesOfPeer;

public class FileServer extends Thread {
	private int portNumber;							//This is the port number that THIS host will run on
	private ServerSocket serverSocket = null;

	public FileServer(int portNumber) {
		this.portNumber = portNumber;
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(portNumber);

			// make this false when you want to disconnect the host
			while (PropertiesOfPeer.peerUp) {
				new FileServerThreadWorkDispatcher(serverSocket.accept()).start();
			}

			// Is this still neccessary since we're closing the socket
			// from the thread???
			serverSocket.close();
		} catch (IOException e) {
			System.err.println("Could not listen on port: "
					+ Integer.toString(portNumber));
			System.exit(-1);
		}
	}
}
