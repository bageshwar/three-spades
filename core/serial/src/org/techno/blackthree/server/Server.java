package org.techno.blackthree.server;

import static org.techno.blackthree.common.event.ConsoleGameEventListener.debug;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.techno.blackthree.common.event.GameEventListener;

/**
 * @author bageshwp
 * */
public class Server {

	public static final int PLAYER_COUNT=8;
	public static final int CARD_COUNT=48;
	public static final int SERVER_POLL_TIME=5000;
	public static final int CLIENT_POLL_TIME=5000;
	
	Controller controller;
	
	ServerSocket socket=null;
	
	boolean serverStarted = false;
	
	public final static int SERVER_PORT = 12346;

	public static void main(String args[]) throws IOException {
		@SuppressWarnings("unused")
		Server server = new Server(args);
		server.init();
		
	}

	public void stop() {
		try {
			controller.stopAllPlayers();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			serverStarted = false;
		}
	}
	
	public boolean isStarted(){
		return serverStarted;
	}
	
	public void addGameEventListener(GameEventListener listener){
		controller.addGameEventListener(listener);
	}
	/**
	 * 
	 * Returns the Controller
	 * */
	public Controller getController(){
		return controller;
	}
	
	private void init() throws IOException {
		// bind a socket

		
			socket = new ServerSocket(SERVER_PORT);
			debug("Listening on port # " + SERVER_PORT);			
			int counter = 0;
			serverStarted = true;
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
	
	public void start() throws IOException{
		init();
	}
}
