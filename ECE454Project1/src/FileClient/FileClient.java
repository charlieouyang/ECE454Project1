package FileClient;

import java.net.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

//This class will spawn all of the connection threads that will connect to other hosts

public class FileClient extends Thread {
	private HashMap<String, Integer> ipAddrPortNumMapping;
	private HashMap<String, Socket> ipAddrSocketMapping;
	
	public FileClient(HashMap<String, Integer> ipAddrPortNumMapping) {
		this.ipAddrPortNumMapping = ipAddrPortNumMapping;
		ipAddrSocketMapping = new HashMap<>();
	}
	
	@Override
	public void run() {
		try {
			ExecutorService pool = Executors.newFixedThreadPool(10);	//Not sure about the 10 here....
			Set<Future<Integer>> set = new HashSet<Future<Integer>>();
			
			Iterator it = ipAddrPortNumMapping.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        
		        String ipAddress = (String)pairs.getKey();
		        int portNumber = (Integer)pairs.getValue();
		        
		        Socket socket = new Socket(ipAddress, portNumber);
		        ipAddrSocketMapping.put(ipAddress, socket);				//This will keep track of every socket and their ipAddr
		        
		        Callable<Integer> clientThread = new FileClientConnection(socket);
		        Future<Integer> futureResult = pool.submit(clientThread);
		        set.add(futureResult);
		        
		        it.remove(); // avoids a ConcurrentModificationException
		    }
		    
		    //Iterate through the results and retrieve...
		    for (Future<Integer> future : set) {
		  	  	//Do something with future.get()
		  	}
		    
		    //After you're done, close the sockets
			
		} catch (Exception e) {
			System.err.println("Screwed up on FileClient");
			System.exit(-1);
		}
	}

}
