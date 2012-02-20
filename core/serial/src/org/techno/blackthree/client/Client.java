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
	 * Flag tells if the client is connected.
	 * */
	boolean connected =true;
	
	ArrayList<Move> currentBoard;
	
	/**
	 * This is the listener which will consume the events,
	 * and act accordingly.
	 * */
	ArrayList<GameEventListener> eventListeners = new ArrayList<GameEventListener>();
	
	
	
	/**
	 * @return the eventListener
	 */
	public ArrayList<GameEventListener> getEventListener() {
		return eventListeners;
	}

	/**
	 * @param eventListener the eventListener to set
	 */
	public void addEventListener(GameEventListener eventListener) {
		this.eventListeners.add( eventListener);
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


		
		s = new String[]{"localhost"};
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
	
	private Client(String host, int port, GameEventListener listener){
		
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
			disconnected();
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
		else if(s.equals(Codes.BOARD_UPDATE))
			updateBoard();
		else if(s.equals(Codes.DISCONNECTED))
			disconnected();
	}

	public void disconnect() throws IOException{
		this.clientSocket.close();
		connected=false;
	}
	
	public boolean isConnected(){
		return connected;
	}
	
	private void disconnected() {
		try {
			disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		fireEvent(new GameEvent(Codes.DISCONNECTED,null));
	}

	private void updateBoard() throws IOException, ClassNotFoundException {
		//read the board
		currentBoard = (ArrayList<Move>) input.readObject();
		fireEvent(new GameEvent(Codes.BOARD_UPDATE,currentBoard));
		
		//need to log the current card played, ideally it should be the last item in the list.
		if(currentBoard.size()!=0)
			debug(currentBoard.get(currentBoard.size()-1).toExtendedString());
		
	}

	private void updateScore() throws IOException, ClassNotFoundException {
		Integer score = (Integer) input.readObject();
		this.player.setScore(score);
		fireEvent(new GameEvent(Codes.SCORE_UPDATE,null));
		debug("You get "+score+" points");
	}

	private void makeAMove() throws IOException {
		
		//generate a random no,
		//and send the same as move. Remove the same from the list.
		
		//TODO: Get the card from the user
		int idx = (int)(Math.random()*player.getCards().size());
		
		Move m = new Move(player.getCards().get(idx));
		m.setPlayer(this.getPlayer().getName());
		GameEvent event =  new GameEvent(Codes.MOVE,currentBoard);
		fireEvent(event);
		if(eventListeners.size()!=0){
			Card c = (Card) event.getResponse();
			m = new Move(c);
			m.setPlayer(this.getPlayer().getName());
			idx = player.getCards().indexOf(c);
		}
		
		output.writeObject(m);		
		output.flush();
		output.reset();
		player.getCards().remove(idx);		
		
		//update the controls
		fireEvent(new GameEvent(Codes.ACCEPT_HAND,player.getCards()));
	}

	private void updateRoundDetails() throws IOException, ClassNotFoundException {

		roundParams = (RoundParameters) input.readObject();

		debug(player.getName() + "\t" + roundParams);

	}

	private void debug(String msg) {
		System.out.println(msg);
		fireEvent(new GameEvent(Codes.LOG_MESSAGE,msg));
		
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
		
		GameEvent event = new GameEvent(Codes.KINGS_SPEECH,null);
		fireEvent(event);
		
		//structure for non-simulated environments
		if(this.getEventListener().size()!=0){
			suite = (Suite) ((Object[])event.getResponse())[0];
			partnerCards = (Card[]) ((Object[])event.getResponse())[1];
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
		debug(player.getName() + ">> Deal " + deal);
		
		//since this is a new deal, need to reset the roundParams
		roundParams = new RoundParameters();
		
		fireEvent(new GameEvent(Codes.ACCEPT_HAND,deal));

	}
	
	private void fireEvent(GameEvent gameEvent){
		for(GameEventListener l:eventListeners){
			l.consumeGameEvent(gameEvent);
		}
			
			
	}

	private void processAdhocMessage() throws IOException, ClassNotFoundException {
		String msg = (String) input.readObject();
		debug("<<ServerCast>> " + msg);

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

		debug(player.getName() + ">> Bid Update: " + roundParams);

	}

	private void placeBid() throws IOException, InterruptedException, ClassNotFoundException {
		Integer bid = (int) (Math.random() * 100);
		bid = bid + 150;

		if (tempBidForTest++ == 2)
			bid = -1;

		if (bid < roundParams.getMaxBid()) {
			debug(this.player.getName() + " can't place a bid, coz its too high");
			bid = -1;
		}

		
		 
		
		GameEvent event = new GameEvent(Codes.BID,null);
		fireEvent(event);
		Integer  mybid = (Integer) event.getResponse();		
		if(mybid==null)
			mybid=bid;
		debug("Me, " + this.player.toString() + " am placing a bid of " + mybid);
		output.writeObject(mybid);
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

		debug(p + " has joined the game.");

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

	/**
	 * @return the roundParams
	 */
	public RoundParameters getRoundParams() {
		return roundParams;
	}

	private void initStreams() throws IOException, ClassNotFoundException, InterruptedException {

		debug("Initing Client streams...");
		input = new ObjectInputStream(clientSocket.getInputStream());
		output = new ObjectOutputStream(clientSocket.getOutputStream());

		// Object s = input.readObject();
		// System.out.println("read "+s);
		/*
		 * if (Codes.OK.equalsIgnoreCase(s.toString()))
		 * System.out.println("Connection Successfull");
		 */

		waitForOK();
		debug("Connection Successful at client");
		connected=true;
		output.writeObject(Codes.OK);
		output.flush();
		output.reset();

		debug("Connection Successfull at server");
		debug("Hi, this is " + player.getName());
	}
	
	public void poll(){
		//start a thread and keep polling the client
		new Thread(new Runnable(){

			@Override
			public void run() {				
				while(connected){
					connected=clientSocket.isConnected();
					try {
						Thread.sleep(Server.CLIENT_POLL_TIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				//disconnected.
				disconnected();
			}
			
			
		}).start();
		
	}
}
