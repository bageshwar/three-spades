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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.techno.blackthree.common.Card;
import org.techno.blackthree.common.Codes;
import org.techno.blackthree.common.InvalidDataStreamException;
import org.techno.blackthree.common.Player;

/**
 * @author bageshwp
 * 
 */
public class Process implements Runnable {

	private static final int POLL_TIME = 3000; // in ms;

	/**
	 * The player this process caters too.
	 * */
	private Player player;
	private Socket socket;

	private boolean tired = false;

	private HashMap<String, Integer> gameSummary;
	private HashMap<String, Integer> roundSummary;

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

		System.out.println("New Process at server started for player ");

		try {

			while (!tired) {
				Thread.sleep(POLL_TIME);
			}

			// sendOutHand();

			// sendRoundSummary();

			// we are tired.
			// send out the summary to the player.
			sendGameSummary();

		} catch (IOException e) {
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
	 * 
	 * @throws IOException
	 * */
	public void sendGameSummary() throws IOException {
		output.writeObject(Codes.GAME_OVER);
		output.writeObject(gameSummary);
		output.flush();
	}

	public boolean isTired() {
		return tired;
	}

	public void setGameSummary(HashMap<String, Integer> gameSummary) {
		this.gameSummary = gameSummary;
	}

	/**
	 * Call this method to terminate this thread after a pack to end game safely
	 * and display the results.
	 * */
	public void setTired(boolean tired) {
		this.tired = tired;
	}

	private void initStreams() throws IOException, ClassNotFoundException {

		System.out.println("Initing Server streams...");
		output = new ObjectOutputStream(socket.getOutputStream());

		input = new ObjectInputStream(socket.getInputStream());

		output.writeObject(Codes.OK);
		output.flush();
		// System.out.println("wrote "+Codes.OK);
		String s = (String) input.readObject();
		if (Codes.OK.equalsIgnoreCase(s))
			System.out.println("Connection Successfull");

	}

	private void initPlayerInfo() throws IOException, ClassNotFoundException, InvalidDataStreamException {
		/**
		 * Assuming that we have the streams with us now.
		 * */

		output.writeObject(Codes.GIVE_PLAYER);
		output.flush();
		Object o = input.readObject();
		try {
			player = (Player) o;
			System.out.println("Server recieved new player: " + player.getName());

			output.writeObject(Codes.OK);
			output.flush();
		} catch (ClassCastException cce) {
			cce.printStackTrace();
			throw new InvalidDataStreamException("Player Details expected");
		}

		monitor = true;

	}

	public Process(Socket socket) throws IOException, ClassNotFoundException, InvalidDataStreamException {
		this.socket = socket;

		initStreams();
		initPlayerInfo();

		new Thread(this).start();

	}

	synchronized public void sendMessage(String msg) throws IOException {

		output.writeObject(Codes.ADHOC_MESSAGE);
		output.writeObject(msg);
		output.flush();
	}

	public void sendPlayerUpdate(String name) throws IOException {

		output.writeObject(Codes.PLAYER_UPDATE);
		output.writeObject(name);
		output.flush();
	}

	public void sendAllPlayersUpdate(Process[] players) {
		for (Process p : players) {

			// to filter out null players and the currently joined player
			if (p == null)
				continue;

			try {
				this.sendPlayerUpdate(p.getPlayer().getName());
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

	@Override
	public String toString() {
		return player.getName()+"@"+socket.getInetAddress().getCanonicalHostName();
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((player == null) ? 0 : player.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Process other = (Process) obj;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		return true;
	}

	public void distributeDeal(ArrayList<Card> list) throws IOException {
		
		output.writeObject(Codes.ACCEPT_HAND);
		output.writeObject(list);
		output.flush();
		
	}

}
