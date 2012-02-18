/**
 * 
 */
package org.techno.blackthree.server;

import static org.techno.blackthree.common.event.ConsoleGameEventListener.debug;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.techno.blackthree.common.Card;
import org.techno.blackthree.common.Codes;
import org.techno.blackthree.common.InvalidDataStreamException;
import org.techno.blackthree.common.Round;
import org.techno.blackthree.common.RoundParameters;
import org.techno.blackthree.common.event.ConsoleGameEventListener;
import org.techno.blackthree.common.event.GameEvent;
import org.techno.blackthree.common.event.GameEventListener;

/**
 * The server sends the control to this class, which does all the syncing
 * 
 * @author bageshwp
 * 
 */
public class Controller implements Runnable {


	ArrayList<GameEventListener> gameEventListeners;
	
		
	private int size;
	
	private boolean tired = false;
	
	private HashMap<Process,Integer> scores;

	public boolean isTired() {
		return tired;
	}

	public void setTired(boolean tired) {
		this.tired = tired;
	}

	/**
	 * An array, since number of players is fixed.
	 * */
	private Process[] players;

	// Added for future use.
	private ThreadGroup threadGroup = null;

	private ArrayList<Round> rounds;

	private Round currentRound = null;

	public int getSize() {
		return size;
	}

	
	
	/**
	 * A synchronous method, that initializes the player, 
	 * and sends other player updates about this.
	 * */
	@SuppressWarnings("unchecked")
	public void addPlayer(final int index,final Socket s) {
		
				
		
				// create a process
				Process p = null;
				try {

					// init the streams and get the player info
					p = new Process(s,index);

					// send all the initial player's update to this player
					p.sendAllPlayersUpdate(players);
					//p.sendMessage("Waiting for " + (size - index - 1) + "/" + size + " players ");
					fireGameEvent(new GameEvent(Codes.PLAYER_UPDATE,Arrays.asList(
							index,Codes.PLAYER_CONNECTED,p.getPlayer().getName(),s.getInetAddress().getCanonicalHostName()) 
							));
					
					sendPlayerUpdate(p.getPlayer().getName());
					sendMessage("Waiting for " + (size - index - 1) + "/" + size + " players ");

				} catch (IOException e) {
					
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					
					e.printStackTrace();
				} catch (InvalidDataStreamException e) {
					
					e.printStackTrace();
				}

				// adding this player to the list if all goes well.
				if (p != null) {
					debug("Player # " + index + " joined the game");
					players[index] = p;
					
					fireGameEvent(new GameEvent(Codes.PLAYER_UPDATE,Arrays.asList(
							index,Codes.PLAYER_JOINED,p.getPlayer().getName(),s.getInetAddress().getCanonicalHostName()) 
					));
					
					//start a new thread for polling.
					new Thread(new Runnable(){

						@Override
						public void run() {
							boolean connected=true;
							while(connected){
								connected = s.isConnected();
								try {
									Thread.sleep(Server.SERVER_POLL_TIME);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							//disconnected
							fireGameEvent(new GameEvent(Codes.PLAYER_UPDATE,Arrays.asList(
									index,Codes.PLAYER_DISCONNECTED,"N/A","N/A") 
							));
							
						}}).start();
				}
			}


	protected void sendPlayerUpdate(final String name) {

		for (Process p : players) {

			// to filter out null players and the currently joined player
			if (p == null)
				continue;

			try {
				p.sendPlayerUpdate(name);
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	
	 public void addGameEventListener(GameEventListener l) {
		 gameEventListeners.add( l);
	 }

	 public void removeGameEventListener(GameEventListener l) {
		 gameEventListeners.remove( l);
	 }

	public void fireGameEvent(GameEvent gameEvent){
		System.out.println("Game Event: "+gameEvent);
		for(GameEventListener gel: gameEventListeners ){
			gel.consumeGameEvent(gameEvent);
		
		}
	}

	
	public Controller(int size) {

		this.size = size;
		players = new Process[size];
		rounds = new ArrayList<Round>();
		threadGroup = new ThreadGroup("PlayersProcessThreadGroup");
		scores = new HashMap<Process,Integer>();
		gameEventListeners = new ArrayList<GameEventListener>();
		ConsoleGameEventListener.addGameEventListeners(gameEventListeners);
	}

	/**
	 * The Actual Game Thread
	 * */
	@Override
	public void run() {

		// sleep for some time, so that the latest player can get in sync
		boolean allInitialized = false;
		while (!allInitialized) {
			allInitialized = true;
			for (Process p : players) {
				if (p == null) {
					allInitialized = false;
					// waiting for 2 seconds for all the players to initialise.
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
					break;
				}
			}
		}

		fireGameEvent(new GameEvent(Codes.ALL_PLAYERS_JOINED));

		try {

			sendMessage("All " + size + " players have joined.Starting the game...");
			
			// this will remain in the loop

			// while(!isTired()){

			for(int x=0;x<3;x++){
			// if this is the first round, pass 0 as initial player,
			// else pass the last rounds first player as initial player
			currentRound = new Round(players, (currentRound == null ? 0 : currentRound.getInitialPlayer() + 1
					% this.size));
			rounds.add(currentRound);
			this.distributeDeal();
			this.requestForBid();

			// after the bid is complete, the king will declare the 3 partner
			// cards, and triumph
			players[currentRound.getRoundParameters().getKing()].requestPartnerCardsAndTriumph(currentRound);

			// now we have the round parameters,server cast it to all.
			this.sendPartnerCardsAndTriumph();

			currentRound.assignPartners();
			
			//everybody should start playing now, starting from king and moving forward thereafter.
			
			currentRound.playBoard();
			//send the current round scores
			this.sendScores();
			 }
			summarizeRounds();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}

		
	}

	private void sendScores() throws IOException {
		
		//iternate through the current round's scores, and
		//send the same to every player.
		
		for(Entry<Process,Integer> entry:currentRound.getScores().entrySet()){
			entry.getKey().sendScores(entry.getValue());
		}
		
	}

	private void summarizeRounds() {
		for(Process p:players){
			scores.put(p,0);
		}
		int roundScore=0;
		for(Round r:rounds){
			for(Process p :players){
				roundScore =r.getScores().get(p);
				scores.put(p, scores.get(p)+roundScore);
			
			}
		}
		debug(scores.toString());
	}

	private void sendPartnerCardsAndTriumph() throws IOException {

		debug("Round Parameters : "+currentRound.getRoundParameters().toString());
		for (Process p : players) {
			if (p == null)
				continue;

			p.sendPartnerCardsAndTriumph(currentRound.getRoundParameters());
		}

	}

	private void requestForBid() throws IOException, ClassNotFoundException, InterruptedException {
		/*
		 * for(Process p:players){ p.requestForBid(); }
		 */
		// 0 means need to start bid process
		int nextPlayerToBid = currentRound.getNextPlayerToBid(0);
		int bid = 0;
		while (nextPlayerToBid != -1) {
			bid = players[nextPlayerToBid].requestForBid();
			nextPlayerToBid = currentRound.getNextPlayerToBid(bid);

			/*
			 * send this bid update to all the other players too ! If the
			 * current bid is the maxBid
			 */
			if (bid == currentRound.getRoundParameters().getMaxBid())
				this.sendBidUpdate(currentRound.getRoundParameters());

			Thread.sleep(500);

		}

		/*this.sendMessage("You have to play for " + players[currentRound.getKing()] + "'s bid of "
				+ currentRound.getMaxBid());*/
	}

	private void sendBidUpdate(RoundParameters roundParams) throws IOException {
		debug("Sending Bid update");
		//System.out.println(roundParams);
		for (Process p : players) {
			if (p == null)
				continue;

			p.sendBidUpdate(roundParams);
		}

	}

	/**
	 * Broadcast a message to all the players.
	 * 
	 * @throws IOException
	 * */
	synchronized private void sendMessage(final String msg) throws IOException {

		for (Process p : players) {
			if (p == null)
				continue;

			p.sendMessage(msg);
		}
	}

	private void distributeDeal() throws InterruptedException, IOException {

		HashMap<Process, ArrayList<Card>> pack = new HashMap<Process, ArrayList<Card>>();

		Card[] freshPack = Card.getFreshPack();

		int random = getRandomNumber();

		ArrayList<Card> deal = null;

		for (int i = 0; i < freshPack.length / size; i++) {
			for (int j = 0; j < size; j++) {
				while (freshPack[random] == null)
					random = getRandomNumber();

				deal = pack.get(players[j]);
				if (deal == null)
					deal = new ArrayList<Card>();
				deal.add(freshPack[random]);
				pack.put(players[j], deal);

				freshPack[random] = null;
			}
		}

		// the deal is ready.. please distribute it.
		
		for (Process p : players) {
			p.distributeDeal(pack.get(p));

		}
	}

	private int getRandomNumber() {
		return (int) (Math.random() * 48);
	}

	
}
