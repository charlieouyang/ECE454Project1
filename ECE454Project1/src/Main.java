

public class Main {
	public static void main(String [ ] args)
	{
		//Main execution point
		
		//1) Invoke the File Server Thread
			//Pass in an int for the port number used for the socket
		
		//2) Invoke the File Client Thread
		
		//3) Deal with graceful shut down?
		
		
		
		//To Do:
		
		//FileServer
		//Add a protocol class to handle the messages (request for new file, file list, chunks, etc etc.)
		
		//File Client
		//Implement a hashmap<string, int> for IP address and port number
		//Add a protocol to handle a list of other host names (IPs) and list of port numbers
		//Problem: Synchronization issues (server isn't up yet and client thread is already trying to connect)
	}

}
