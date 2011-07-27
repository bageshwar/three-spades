/**
 * 
 */
package org.techno.blackthree.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.techno.blackthree.common.Codes;
import org.techno.blackthree.common.Player;
import org.techno.blackthree.server.Server;

/**
 * @author bageshwp
 * 
 */
public class Client implements Runnable {

	Socket clientSocket = null;

	/**
	 * This monitor blocks any thread to access this thread's info until the
	 * stream and vital parameters have been successfully initialized.
	 * */
	private boolean monitor = false;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	private boolean connectionOK = false;
	private Player player;

	
	private boolean tired = false;
	
	public boolean isTired() {
		return tired;
	}

	public void setTired(boolean tired) {
		this.tired = tired;
	}

	public static void main(String s[]) {		
		
		Client client = null;

		if (s == null) {
			System.out.println("Host/port missing");
			System.exit(-1);
		} else if (s.length == 1) {
			try {
				client = new Client(s[0]);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else

		if (s.length == 2) {
			try {
				client = new Client(s[0], Integer.parseInt(s[1]));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("Invalid Number of parameters:  Client host port");
		}

		new Thread(client).start();

	}

	private Client(String host) throws UnknownHostException, IOException {

		this(host, Server.SERVER_PORT);

	}

	private Client(String host, int port) throws UnknownHostException, IOException {
		clientSocket = new Socket(host, port);
		player = new Player("Player# "+(int)(Math.random()*200));
	}

	@Override
	public void run() {
		 
		try {
			initStreams();
			
			/**
			 * Flow
			 * 1. Wait for OK
			 * 2. Send player details
			 * 3. Get Players update
			 * --------
			 * 4. Wait for round start
			 * 5. Get Card details
			 * 
			 * */
			
				while(!isTired()){
					Thread.sleep(200);			
					String s = (String) input.readObject();
					handleInput(s);
				}
				
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * The actual orchestrator for client
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 * */
	private void handleInput(String s) throws IOException, ClassNotFoundException, InterruptedException {
		
		if(s==null)
			return; //do nothing
		
		if(s.equals(Codes.GIVE_PLAYER))
			initPlayerDetails();
		else if (s.equals(Codes.PLAYER_UPDATE))
			updatePlayersDetails();
		else if(s.equals(Codes.BID))
			placeBid();
		else if(s.equals(Codes.BID_UPDATE))
			updateBidDetails();
		else if(s.equals(Codes.GAME_OVER))
			gameOver();
		else if(s.equals(Codes.ADHOC_MESSAGE))
			processAdhocMessage();
	}

	private void processAdhocMessage() throws IOException, ClassNotFoundException {
		String msg = (String) input.readObject();
		System.out.println(msg);
		
	}

	private void gameOver() {
		//process the game over update
		setTired(true);
		
	}

	private void updateBidDetails() {
		// TODO Auto-generated method stub
		
	}

	private void placeBid() throws IOException, InterruptedException, ClassNotFoundException {
		Integer bid = (int)(Math.random()*100);
		bid = bid+150;
		
		output.writeObject(bid);
		waitForOK();
		
	}

	
	/**
	 * Utility method to wait for the OK status.
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 * */
	private void waitForOK() throws IOException, ClassNotFoundException, InterruptedException{
		String s ;
		s = (String) input.readObject();
		while(!s.equals(Codes.OK)){
			Thread.sleep(200);
			s = (String) input.readObject();
		}
	}
	
	/**
	 * Update player details.
	 *  A single string with the name of the player is expected.
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 * */
	private void updatePlayersDetails() throws IOException, ClassNotFoundException {
		
		String p = (String) input.readObject();
		
		System.out.println(p+" has joined the game.");
		
		//utput.wriite
		//no need to notify server of ok
		
	}

	private void initPlayerDetails() throws IOException, ClassNotFoundException, InterruptedException {
		
			output.writeObject(this.player);
			output.flush();
			waitForOK();
			
		
	}

	private void initStreams() throws IOException, ClassNotFoundException, InterruptedException {

		System.out.println("Initing Client streams...");
		input = new ObjectInputStream(clientSocket.getInputStream());
		output = new ObjectOutputStream(clientSocket.getOutputStream());

		
		 
		//Object s = input.readObject();
		//System.out.println("read "+s);
		/*if (Codes.OK.equalsIgnoreCase(s.toString()))
			System.out.println("Connection Successfull");*/
		
		waitForOK();
		System.out.println("Connection Successful at client");
		output.writeObject(Codes.OK);
		output.flush();

		System.out.println("Connection Successfull at server");
		System.out.println("Hi, this is "+player.getName());
		
	}
}
