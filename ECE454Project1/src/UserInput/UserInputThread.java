package UserInput;

import java.io.Console;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

import Data.PropertiesOfPeer;
import FileClient.CloseThisConnectionThread;
import FileServer.FileServerThreadWorkDispatcher;

public class UserInputThread extends Thread {
	public UserInputThread() {
	}

	public void run() {
		try {
			while (true){
				Scanner scanner = new Scanner (System.in);
				System.out.print("Enter your name");  
				String input = scanner.next(); 
				
				if (input.equals("shutdown")){
					//Shutdown the server!
					CloseThisConnectionThread closePeerThread = new CloseThisConnectionThread();
					closePeerThread.start();
				}
				
			}
		} catch (Exception e) {
			System.err.println("User console error");
			e.printStackTrace();
			System.exit(-1);
		}
	}
}