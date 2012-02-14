/**
 * 
 */
package org.techno.blackthree.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import org.techno.blackthree.common.Board;
import org.techno.blackthree.common.Card;
import org.techno.blackthree.common.Codes;
import org.techno.blackthree.common.InvalidDataStreamException;
import org.techno.blackthree.common.Move;
import org.techno.blackthree.common.Player;
import org.techno.blackthree.common.Round;
import org.techno.blackthree.common.RoundParameters;

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

	private int sequenceNo;
	
	

	private boolean tired = false;

	/**
	 * Scores of each player for each round.
	 * */
	private ArrayList<HashMap<String, Integer>> gameSummary;
	
	/**
	 * Scores of current round
	 * */
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

			 sendOutHand();

			// sendRoundSummary();

			// we are tired.
			// send out the summary to the player.
			sendGameSummary();

		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}

	}

	
	public void sendOutHand() {
		

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
		output.reset();
	}

	public boolean isTired() {
		return tired;
	}
	
	/**
	 * @return the sequenceNo
	 */
	public int getSequenceNo() {
		return sequenceNo;
	}

	
	public void setGameSummary(ArrayList<HashMap<String, Integer>> gameSummary) {
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
		output.reset();
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
		output.reset();
		Object o = input.readObject();
		try {
			player = (Player) o;
			System.out.println("Server recieved new player: " + player.getName());

			output.writeObject(Codes.OK);
			output.flush();
			output.reset();
		} catch (ClassCastException cce) {
			cce.printStackTrace();
			throw new InvalidDataStreamException("Player Details expected");
		}

		monitor = true;

	}

	public Process(Socket socket, int sequence) throws IOException, ClassNotFoundException, InvalidDataStreamException {
		this.socket = socket;
		this.sequenceNo = sequence;
		initStreams();
		initPlayerInfo();

		new Thread(this).start();

	}

	synchronized public void sendMessage(String msg) throws IOException {

		output.writeObject(Codes.ADHOC_MESSAGE);
		output.writeObject(msg);
		output.flush();
		output.reset();
	}

	public void sendPlayerUpdate(String name) throws IOException {

		output.writeObject(Codes.PLAYER_UPDATE);
		output.writeObject(name);
		output.flush();
		output.reset();
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

	/**
	 * Send the client his deal
	 * */
	public void distributeDeal(ArrayList<Card> list) throws IOException {
		player.setCards(list);
		output.writeObject(Codes.ACCEPT_HAND);
		output.writeObject(list);
		output.flush();
		output.reset();
	}

	/**
	 * Request the client to place a bid,
	 * and then read it.
	 * @throws ClassNotFoundException 
	 * */
	public int requestForBid() throws IOException, ClassNotFoundException {
		output.writeObject(Codes.BID);
		output.flush();
		Integer bid = (Integer) input.readObject();
		output.writeObject(Codes.OK);
		output.reset();
		return bid;
	}

	public void sendBidUpdate(RoundParameters roundParams) throws IOException {
		//Send the limited round parameters
		output.writeObject(Codes.BID_UPDATE);
		
		/**
		 * Fix for serialization issues with same object
		 * */
	/*	RoundParameters r = new RoundParameters();
		r.setMaxBid(roundParams.getMaxBid());
		r.setKingPlayer(roundParams.getKingPlayer());
	*/	
		output.writeObject(roundParams);
		output.flush();
		output.reset();
		
	}

	/**
	 * @param currentRound The current round,where the cards and triumph details will be set
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * */
	public void requestPartnerCardsAndTriumph(Round currentRound) throws IOException, ClassNotFoundException {
		
		//send request code
		output.writeObject(Codes.KINGS_SPEECH);
		output.flush();
		output.reset();
		
		RoundParameters roundParams =  (RoundParameters) input.readObject();
		/**
		 * The above instance of RoundParameters contains 
		 * only the triumph and partner cards.
		 * */
		
		currentRound.getRoundParameters().setPartnerCards(roundParams.getPartnerCards());
		currentRound.getRoundParameters().setTriumph(roundParams.getTriumph());
		
		//send the ok status.client waits for the same.
		output.writeObject(Codes.OK);
		output.flush();
		output.reset();
	}

	public void sendPartnerCardsAndTriumph(RoundParameters roundParams) throws IOException {
		/**
		 * Send the code to update the round parameters
		 * Send the round parameters.
		 * These are the final set of round parameters.
		 */
		output.writeObject(Codes.ROUND_PARAMETERS_UPDATE);		
		output.writeObject(roundParams);
		output.flush();
		output.reset();
		
	}

	/**
	 * Request the player to make a move.
	 * @return The move
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * */
	public Move makeAMove() throws IOException, ClassNotFoundException {
		
		//send the make a move code,
		//get the move
				
		output.writeObject(Codes.MOVE);		
		output.flush();
		output.reset();
		Move m = (Move) input.readObject();
		
		return m;
	}

	public void sendScores(Integer value) throws IOException {
		//send the score
		output.writeObject(Codes.SCORE_UPDATE);
		output.writeObject(value);
		output.flush();
		output.reset();
		
		
	}

	public void boardUpdate(ArrayList<Move> m) throws IOException {
		//before the player can move, he has to know the current board state
		output.writeObject(Codes.BOARD_UPDATE);
		
		output.writeObject(m);
		output.flush();
		output.reset();
	}

}
