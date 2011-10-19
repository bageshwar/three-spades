package org.techno.blackthree.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author bageshwp
 * */
public class Server {

	Controller controller;
	
	
	
	public final static int SERVER_PORT = 12346;

	public static void main(String args[]) {
		@SuppressWarnings("unused")
		Server server = new Server(args);
		server.init();
		
	}

	/**
	 * 
	 * Returns the Controller
	 * */
	public Controller getController(){
		return controller;
	}
	
	private void init() {
		// bind a socket

		try {
			ServerSocket socket = new ServerSocket(SERVER_PORT);
			System.out.println("Listening on port # " + SERVER_PORT);			
			int counter = 0;
			while (counter < controller.getSize()) {
				//System.out.println(counter+"...");
				Socket s = socket.accept();
				// add this to the list of processes.
				controller.addPlayer(counter, s);

				counter++;
			}

			socket.close();
			System.out.println("All players have joined...");
			
			new Thread(controller).start();			
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}

	/**
	 * prevent instantiation
	 * 
	 * @param args
	 * */
	public Server(String[] args) {
		size = 8;
		try {
			if (args != null && args.length != 0) {
				size = Integer.parseInt(args[0]);				
			}
		} catch (NumberFormatException nfe) {
			System.out.println("Invalid number of players specified in startup: " + args[0]);
			System.exit(0);
		}
		controller = new Controller(size);
	}
	int size;
	
	public void start(){
		init();
	}
}
