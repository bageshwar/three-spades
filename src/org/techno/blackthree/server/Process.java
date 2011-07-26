/**
 * 
 */
package org.techno.blackthree.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

import org.techno.blackthree.common.Codes;
import org.techno.blackthree.common.InvalidDataStreamException;
import org.techno.blackthree.common.Player;

/**
 * @author bageshwp
 * 
 */
public class Process implements Runnable {

	private static final int POLL_TIME = 3000; //in ms;
	
	/**
	 * The player this process caters too.
	 * */
	private Player player;
	private Socket socket;

	private boolean tired = false;
	
	private HashMap<String, Integer> gameSummary;
	private HashMap<String,Integer> roundSummary;
	
	/**
	 * This monitor blocks any thread to access this thread's info until the
	 * stream and vital parameters have been successfully initialized.
	 * */
	private boolean monitor = false;
	private ObjectInputStream input;
	private ObjectOutputStream output;

	public Player getPlayer() {
		while (!monitor)
			try {
				Thread.sleep(500);
				System.out.println("Sleeping for monitor to be notified.");
			} catch (InterruptedException e) {

				e.printStackTrace();
			}

		return player;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		try {
			
			initPlayerInfo();
			
			while(!tired){				
				Thread.sleep(POLL_TIME);				
			}
			
			sendOutHand();
			
			sendRoundSummary();
			
			//we are tired.
			//send out the summary to the player.
			sendGameSummary();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidDataStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendOutHand() {
		// TODO Auto-generated method stub
		
	}

	public void sendRoundSummary() {
		
		
	}

	/**
	 * Summarize the results and push it to the player.
	 * @throws IOException 
	 * */
	public void sendGameSummary() throws IOException {
		output.writeBoolean(tired);
		
	}
	
	

	public boolean isTired() {
		return tired;
	}

	public void setGameSummary(HashMap<String, Integer> gameSummary) {
		this.gameSummary = gameSummary;
	}

	
	/**
	 * Call this method to terminate this thread after a pack to end game safely and display the results.
	 * */
	public void setTired(boolean tired) {
		this.tired = tired;
	}

	private void initStreams() throws IOException {

		System.out.println("Initing Server streams...");		
		output = new ObjectOutputStream(socket.getOutputStream());
		
		//input = new ObjectInputStream(socket.getInputStream());

		output.writeObject(Codes.OK);
		output.flush();
		System.out.println("wrote "+Codes.OK);
/*		String s = input.readUTF();
		if(Codes.OK.equalsIgnoreCase(s))
			System.out.println("Connection Successfull");
*/		
	}

	private void initPlayerInfo() throws IOException, ClassNotFoundException, InvalidDataStreamException {
		/**
		 * Assuming that we have the streams with us now.
		 * */
		
		output.writeChars(Codes.GIVE_PLAYER);
		Object o = input.readObject();
		try{
		player = (Player) o;
		}catch(ClassCastException cce){
			cce.printStackTrace();
			throw new InvalidDataStreamException("Player Details expected");
		}
		
		monitor = true;

	}

	public Process(Socket socket) throws IOException, ClassNotFoundException, InvalidDataStreamException {
		this.socket = socket;
		initStreams();
		//initPlayerInfo();
		
	}

	synchronized public  void sendMessage(String msg) throws IOException{
	
		output.writeChars(msg);
	}
	
}
