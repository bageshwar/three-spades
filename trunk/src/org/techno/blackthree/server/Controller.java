/**
 * 
 */
package org.techno.blackthree.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import org.techno.blackthree.common.Card;
import org.techno.blackthree.common.Face;
import org.techno.blackthree.common.InvalidDataStreamException;
import org.techno.blackthree.common.Round;
import org.techno.blackthree.common.RoundParameters;
import org.techno.blackthree.common.Suite;

/**
 * The server sends the control to this class, which does all the syncing
 * 
 * @author bageshwp
 * 
 */
public class Controller implements Runnable {

	private int size;

	private boolean tired = false;

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

	public void addPlayer(final int index, final Socket s) {
		new Thread(threadGroup, new Runnable() {

			@Override
			public void run() {

				// create a process
				Process p = null;
				try {

					// init the streams and get the player info
					p = new Process(s);

					// send all the initial player's update to this player
					p.sendAllPlayersUpdate(players);
					p.sendMessage("Waiting for " + (size - index - 1) + "/" + size + " players ");

					Controller.this.sendPlayerUpdate(p.getPlayer().getName());
					Controller.this.sendMessage("Waiting for " + (size - index - 1) + "/" + size + " players ");

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidDataStreamException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// adding this player to the list if all goes well.
				if (p != null) {
					System.out.println("Player # " + index + " joined the game");
					players[index] = p;
				}
			}
		}, "Player#" + index).start();

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

	public Controller(int size) {

		this.size = size;
		players = new Process[size];
		rounds = new ArrayList<Round>();
		threadGroup = new ThreadGroup("PlayersProcessThreadGroup");
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		}

		

		try {

			sendMessage("All " + size + " players have joined.Starting the game...");
			
			// this will remain in the loop

			// while(!isTired()){

			
			// if this is the first round, pass 0 as initial player,
			// else pass the last rounds first player as initial player
			currentRound = new Round(players, (currentRound == null ? 0 : currentRound.getInitialPlayer() + 1
					% this.size));
			this.distributeDeal();
			this.requestForBid();

			// after the bid is complete, the king will declare the 3 partner
			// cards, and triumph
			players[currentRound.getRoundParameters().getKing()].requestPartnerCardsAndTriumph(currentRound);

			// now we have the round parameters,server cast it to all.
			this.sendPartnerCardsAndTriumph();

			// }
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	private void sendPartnerCardsAndTriumph() throws IOException {

		System.out.println("Round Parameters : "+currentRound.getRoundParameters().toString());
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

			Thread.sleep(5000);

		}

		/*this.sendMessage("You have to play for " + players[currentRound.getKing()] + "'s bid of "
				+ currentRound.getMaxBid());*/
	}

	private void sendBidUpdate(RoundParameters roundParams) throws IOException {
		System.out.println("Sending Bid update");
		System.out.println(roundParams);
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

		Card[] freshPack = getFreshPack();

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

		// System.out.println(pack);

		// the deal is ready.. please distribute it.

		System.out.println("It appears that all players have joined");
		for (Process p : players)
			System.out.println(p.getPlayer().toString());

		for (Process p : players) {

			p.distributeDeal(pack.get(p));

		}
	}

	private int getRandomNumber() {
		return (int) (Math.random() * 48);
	}

	private Card[] getFreshPack() {

		ArrayList<Card> pack = new ArrayList<Card>();

		for (Face face : Face.values()) {
			for (Suite suite : Suite.values()) {
				pack.add(new Card(suite, face));
			}
		}

		return pack.toArray(new Card[] {});

	}
}
