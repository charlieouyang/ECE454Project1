package FileServer;
import java.net.*;
import java.io.*;

public class FileServerConnection extends Thread {
	private Socket socket = null;

    public FileServerConnection(Socket socket) {
		this.socket = socket;
    }

    //This thread will do 2 things
    //1) Sending out list of files when requested
    //2) Sending requested files
    public void run() {

		try {
		    
			/*
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		    BufferedReader in = new BufferedReader(
					    new InputStreamReader(
					    socket.getInputStream()));
	
		    String inputLine, outputLine;
		    
		    while ((inputLine = in.readLine()) != null) {
				outputLine = inputLine;
				out.println(outputLine);
				if (outputLine.equals("Bye"))
				    break;
		    }
		    out.close();
		    in.close();
		    */
		    
			
		    socket.close();
	
		} catch (IOException e) {
		    e.printStackTrace();
		}
    }
}
