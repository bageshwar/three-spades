/**
 * 
 */
package org.techno.blackthree.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.techno.blackthree.common.Card;
import org.techno.blackthree.common.Codes;
import org.techno.blackthree.common.Move;
import org.techno.blackthree.common.Player;
import org.techno.blackthree.common.RoundParameters;
import org.techno.blackthree.common.Suite;
import org.techno.blackthree.common.event.GameEvent;
import org.techno.blackthree.common.event.GameEventListener;
import org.techno.blackthree.server.Server;

/**
 * @author bageshwp
 * 
 */
@SuppressWarnings("unused")
public class Client implements Runnable {

	/**
	 * This is the listener which will consume the events,
	 * and act accordingly.
	 * */
	GameEventListener eventListener = null;
	
	/**
	 * @return the eventListener
	 */
	public GameEventListener getEventListener() {
		return eventListener;
	}

	/**
	 * @param eventListener the eventListener to set
	 */
	public void setEventListener(GameEventListener eventListener) {
		this.eventListener = eventListener;
	}

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

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	

	private RoundParameters roundParams = null;

	/**
	 * Temporary,for simulation
	 * */
	private int tempBidForTest = 0;

	private boolean tired = false;

	public boolean isTired() {
		return tired;
	}

	public void setTired(boolean tired) {
		this.tired = tired;
	}

	public static void main(String s[]) {

		for (int i = 0; i < 7; i++) {

			Client client = null;

			if (s == null) {
				System.out.println("Host/port missing");
				System.exit(-1);
			} else if (s.length == 1) {
				try {
					client = new Client(s[0]);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {					
					e.printStackTrace();
				}
			} else

			if (s.length == 2) {
				try {
					client = new Client(s[0], Integer.parseInt(s[1]));
				} catch (NumberFormatException e) {
					
					e.printStackTrace();
				} catch (UnknownHostException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			} else {
				System.out.println("Invalid Number of parameters:  Client host port");
			}

			new Thread(client).start();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public Client(String host) throws UnknownHostException, IOException {

		this(host, Server.SERVER_PORT);

	}

	public Client(String host, int port) throws UnknownHostException, IOException {
		clientSocket = new Socket(host, port);
		player = new Player("Player# " + (int) (Math.random() * 200));
		//roundParams = new RoundParameters();
	}
	
	public Client(String host, int port,String name) throws UnknownHostException, IOException {
		clientSocket = new Socket(host, port);
		player = new Player(name);
		
	}
	
	public Client(String host, int port, GameEventListener listener){
		
	}

	@Override
	public void run() {

		try {
			initStreams();

			/**
			 * Flow 1. Wait for OK 2. Send player details 3. Get Players update
			 * -------- 4. Wait for round start 5. Get Card details
			 * 
			 * */

			while (!isTired()) {
				Thread.sleep(500);
				Object o = null;
				String s;
				try {
					o = input.readObject();
					s = (String) o;
					handleInput(s);
				} catch (OptionalDataException e) {
					System.out.println(o);
					System.out.println("OptionalDataException " + e.length + ":" + e.eof);
					input.reset();
					output.reset();
					e.printStackTrace();
				} catch (StreamCorruptedException e) {
					input.reset();
					output.reset();
					e.printStackTrace();
				} 			
			}

		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}

	}

	/**
	 * The actual orchestrator for client
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 * */
	private void handleInput(String s) throws IOException, ClassNotFoundException, InterruptedException {

		if (s == null)
			return; // do nothing

		if (s.equals(Codes.GIVE_PLAYER))
			initPlayerDetails();
		else if (s.equals(Codes.PLAYER_UPDATE))
			updatePlayersDetails();
		else if (s.equals(Codes.BID))
			placeBid();
		else if (s.equals(Codes.BID_UPDATE)) // TODO: remove bid update and use
			// round parameters update
			updateBidDetails();
		else if (s.equals(Codes.GAME_OVER))
			gameOver();
		else if (s.equals(Codes.ADHOC_MESSAGE))
			processAdhocMessage();
		else if (s.equals(Codes.ACCEPT_HAND))
			acceptDeal();
		else if (s.equals(Codes.KINGS_SPEECH))
			sendPartnerCardsAndTriumph();
		else if (s.equals(Codes.ROUND_PARAMETERS_UPDATE))
			updateRoundDetails();
		else if (s.equals(Codes.MOVE))
			makeAMove();
		else if(s.equals(Codes.SCORE_UPDATE))
			updateScore();
	}

	private void updateScore() throws IOException, ClassNotFoundException {
		Integer score = (Integer) input.readObject();
		this.player.setScore(score);
		
	}

	private void makeAMove() throws IOException {
		
		//generate a random no,
		//and send the same as move. Remove the same from the list.
		
		//TODO: Get the card from the user
		int idx = (int)(Math.random()*player.getCards().size());
		
		Move m = new Move(player.getCards().get(idx));
		
		output.writeObject(m);		
		output.flush();
		output.reset();
		player.getCards().remove(idx);
	}

	private void updateRoundDetails() throws IOException, ClassNotFoundException {

		roundParams = (RoundParameters) input.readObject();

		System.out.println(player.getName() + "\t" + roundParams);

	}

	private void sendPartnerCardsAndTriumph() throws IOException, ClassNotFoundException, InterruptedException {
		/***
		 * Send the suite and then a set of 3 cards. Then wait for ok
		 * */

		int randomSuite = (int) (Math.random() * 4);
		Suite suite = Suite.values()[randomSuite];

		//Algo to generate 3 random partner cards
		Card[] pack = Card.getFreshPack();
		Card[] partnerCards = new Card[3];
		int idx = 0,rnd=0;
		while(idx<3){
			rnd = (int) (Math.random() * 48);
			if(!player.getCards().contains(pack[rnd])){
				partnerCards[idx++] = pack[rnd];
			}
		}
		
		// Utilising the same reference
		roundParams.setTriumph(suite);
		roundParams.setPartnerCards(partnerCards);

		output.writeObject(roundParams);
		output.reset();
		waitForOK();

	}

	@SuppressWarnings("unchecked")
	private void acceptDeal() throws IOException, ClassNotFoundException {
		Object o = input.readObject();
		ArrayList<Card> deal = (ArrayList<Card>) o;
		player.setCards(deal);
		System.out.println(player.getName() + ">> Deal " + deal);
		
		//since this is a new deal, need to reset the roundParams
		roundParams = new RoundParameters();
		
		fireEvent(new GameEvent(Codes.ACCEPT_HAND,deal));

	}
	
	private void fireEvent(GameEvent gameEvent){
		if(eventListener!=null)
			eventListener.consumeGameEvent(gameEvent);
	}

	private void processAdhocMessage() throws IOException, ClassNotFoundException {
		String msg = (String) input.readObject();
		System.out.println(player.getName() + " <<ServerCast>> " + msg);

	}

	private void gameOver() {
		// process the game over update
		setTired(true);

	}

	private void updateBidDetails() throws IOException, ClassNotFoundException {
		roundParams = (RoundParameters) input.readObject();
		/**
		 * The above object would hold limited information. viz. maxBid and
		 * maxBid player's name
		 * */

		System.out.println(player.getName() + ">> Bid Update: " + roundParams);

	}

	private void placeBid() throws IOException, InterruptedException, ClassNotFoundException {
		Integer bid = (int) (Math.random() * 100);
		bid = bid + 150;

		if (tempBidForTest++ == 2)
			bid = -1;

		if (bid < roundParams.getMaxBid()) {
			System.out.println(this.player.getName() + " can't place a bid, coz its too high");
			bid = -1;
		}

		System.out.println("Me, " + this.player.toString() + " am placing a bid of " + bid);

		output.writeObject(bid);
		output.reset();
		waitForOK();

	}

	/**
	 * Utility method to wait for the OK status.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 * */
	private void waitForOK() throws IOException, ClassNotFoundException, InterruptedException {
		String s;
		s = (String) input.readObject();
		while (!s.equals(Codes.OK)) {
			// poll after 200 ms

			Thread.sleep(200);
			s = (String) input.readObject();
		}
	}

	/**
	 * Update player details. A single string with the name of the player is
	 * expected.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * */
	private void updatePlayersDetails() throws IOException, ClassNotFoundException {

		String p = (String) input.readObject();

		System.out.println(p + " has joined the game.");

		fireEvent(new GameEvent(Codes.PLAYER_UPDATE,p));
		
		// utput.wriite
		// no need to notify server of ok

	}

	private void initPlayerDetails() throws IOException, ClassNotFoundException, InterruptedException {

		output.writeObject(this.player);
		output.flush();
		output.reset();
		waitForOK();

	}

	private void initStreams() throws IOException, ClassNotFoundException, InterruptedException {

		System.out.println("Initing Client streams...");
		input = new ObjectInputStream(clientSocket.getInputStream());
		output = new ObjectOutputStream(clientSocket.getOutputStream());

		// Object s = input.readObject();
		// System.out.println("read "+s);
		/*
		 * if (Codes.OK.equalsIgnoreCase(s.toString()))
		 * System.out.println("Connection Successfull");
		 */

		waitForOK();
		System.out.println("Connection Successful at client");
		output.writeObject(Codes.OK);
		output.flush();
		output.reset();

		System.out.println("Connection Successfull at server");
		System.out.println("Hi, this is " + player.getName());
	}

}
