package UserInput;

import java.util.Scanner;

import FileClient.CloseThisConnectionThread;

public class UserInputThread extends Thread {
	public UserInputThread() {
	}

	@Override
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