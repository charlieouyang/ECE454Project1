package FileClient;

import java.util.concurrent.Callable;
import java.io.IOException;
import java.net.*;

//This will take care of each individual connection with another peer
//It will request for a list of files that another peer has

public class FileClientConnection implements Callable {
	private Socket socket;

	public FileClientConnection(Socket socket) throws Exception {
		this.socket = socket;
	}

	//Contact the peer and ask for a list of files
	public Integer call() {
		System.out.println("Connecting...");

		// sock.close();
		return 1;
	}

}
