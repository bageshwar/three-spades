package org.techno.blackthree.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author bageshwp
 * */
public class Server {

	Controller controller;
	public final static int SERVER_PORT = 12346;

	public static void main(String args[]) {
		Server server = new Server(args);
		
	}

	private void init() {
		// bind a socket

		try {
			ServerSocket socket = new ServerSocket(SERVER_PORT);

			System.out.println("Listening on port # " + SERVER_PORT);
			int counter = 0;
			while (counter < controller.getSize()) {
				System.out.println(counter+"...");
				Socket s = socket.accept();
				// add this to the list of processes.
				controller.addPlayer(counter, s);

				counter++;
			}

			new Thread(controller).start();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * prevent instantiation
	 * 
	 * @param args
	 * */
	private Server(String[] args) {

		int size = 8;
		try {
			if (args != null && args.length != 0) {
				size = Integer.parseInt(args[0]);				
			}
		} catch (NumberFormatException nfe) {
			System.out.println("Invalid number of players specified in startup: " + args[0]);
			System.exit(0);
		}
		
		controller = new Controller(size);
		init();

	}
}
