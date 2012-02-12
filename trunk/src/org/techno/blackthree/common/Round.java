/**
 * 
 */
package org.techno.blackthree.common;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.techno.blackthree.server.Process;

/**
 * 
 * This bean holds the data for a single round.
 * 
 * @author bageshwp
 * 
 */
public class Round implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1049786175808878897L;

	RoundParameters roundParams = null;

	/**
	 * Transient because its not required to be serialized.
	 * */
	transient Process[] players;

	transient HashMap<Process, Integer> scores = null;

	/**
	 * The Next player to bid
	 * */
	int nextPlayerToBid;

	/**
	 * Member to hold individual deals
	 * */
	private ArrayList<Board> boards;

	private Board currentBoard;

	/**
	 * These players passed the bid to the next player. When this would equal
	 * players#-1, we would have the king
	 * */
	private int playersPassed = 0;

	/**
	 * The player with whom the round will be started.
	 * */
	int initialPlayer;

	/**
	 * Partners of the king
	 * */
	ArrayList<Process> kingsMen;

	/**
	 * Not partners of the king.
	 * */
	ArrayList<Process> commoners;

	/**
	 * @param _players
	 *            Players
	 * @param _initialPlayer
	 *            Index of initial Player for this round
	 * */
	public Round(Process[] _players, int _initialPlayer) {
		players = _players;
		initialPlayer = _initialPlayer;
		roundParams = new RoundParameters(players.length);
		boards = new ArrayList<Board>();
		scores = new HashMap<Process, Integer>();
	}

	/**
	 * @return the players
	 */
	public Process[] getPlayers() {
		return players;
	}

	/**
	 * @param players
	 *            the players to set
	 */
	public void setPlayers(Process[] players) {
		this.players = players;
	}

	/**
	 * @param response
	 *            The response of the player. {-1:passed,valid int:value}
	 * @return the nextPlayerToBid. Returns -1, if the bid process needs to
	 *         terminate.
	 */
	public int getNextPlayerToBid(int response) {
		// return nextPlayerToBid;

		// if this is the first bid, send the initial player's id to continue !!
		if (response == 0)
			return this.getInitialPlayer();

		// if player passed
		if (response == -1) {
			playersPassed++;
			System.out.println(players[nextPlayerToBid] + " passed");
		} else
			System.out.println(players[nextPlayerToBid] + " bid " + response);

		// if all players passed.
		if (playersPassed == players.length - 1) {
			System.out.println("All players passed.[" + players[roundParams.getKing()].toString() + " >> "
					+ roundParams.getMaxBid() + "]: start the game");
			return -1;
		}

		/**
		 * If this is a legitimate bid check max bid, if yes, set king as
		 * current player, and reset players passed.
		 * */
		if (roundParams.getMaxBid() < response) {
			roundParams.setMaxBid(response);
			roundParams.setKing(nextPlayerToBid);
			playersPassed = 0;
			roundParams.setKingPlayer(players[nextPlayerToBid].getPlayer().getName());
			System.out.println("We have a new King: " + players[nextPlayerToBid].toString());
		}

		// if this is the max bid
		if (roundParams.getMaxBid() == 250) {
			System.out.println("Found Ultimate Max bid[" + players[roundParams.getKing()].toString()
					+ "]: start the game");
			return -1;
		}

		nextPlayerToBid = (nextPlayerToBid + 1) % players.length;
		return nextPlayerToBid;

	}

	/**
	 * @return the initialPlayer
	 */
	public int getInitialPlayer() {
		return initialPlayer;
	}

	/**
	 * @param initialPlayer
	 *            the initialPlayer to set
	 */
	public void setInitialPlayer(int initialPlayer) {
		this.initialPlayer = initialPlayer;
		roundParams.setKing(initialPlayer);
		// this.setNextPlayerToBid(initialPlayer);
	}

	public RoundParameters getRoundParameters() {
		return roundParams;
	}

	/**
	 * @return the scores
	 */
	public HashMap<Process, Integer> getScores() {
		return scores;
	}

	/**
	 * Check the partner cards and assign the players to either side.
	 * */
	public void assignPartners() {

		// this is an array list,because the no of partners might not be equal
		// to the
		// number of partner cards.
		kingsMen = new ArrayList<Process>();
		commoners = new ArrayList<Process>();
		for (Card c : roundParams.getPartnerCards()) {
			for (Process p : players) {				
				if (p.getPlayer().getCards().contains(c)) {
					// this is kings partner
					kingsMen.add(p);
					break;
				}
			}
		}
		// now the kings men have been decided. time to set the commoners
		for (Process p : players) {
			if (!kingsMen.contains(p) && !p.equals(players[roundParams.getKing()])  )
				commoners.add(p);
		}

		System.out.println("King's Men "+kingsMen);
		System.out.println("Commoners "+commoners);
	}

	
	/**
	 * Request the King, to move, and then each player to move , Evaluate the
	 * result. Assign the scores. Move to the next round.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * */
	public void playBoard() throws IOException, ClassNotFoundException {

		int currentPlayerToPlay = roundParams.getKing();
		int len = players.length;
		int noOfBoards = 48 / len;
		Move move = null;
		for (int j = 0; j < noOfBoards; j++) {
			// init board
			currentBoard = new Board(roundParams);
			// add this board to the list of boards.
			boards.add(currentBoard);
			for (int i = 0; i < len; i++) {
				// iterate players.length no of times, access players by idx.
				
				move = players[currentPlayerToPlay].makeAMove();
				// System.out.println("Player moved" +
				// players[currentPlayerToPlay] + " " + move);
				currentBoard.addMove(players[currentPlayerToPlay], move);
				// -->next player
				currentPlayerToPlay = (currentPlayerToPlay + 1) % len;

				// send this move to all the other players
				// TODO: Move update
				boardUpdate(currentBoard);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			System.out.println(currentBoard.getMoves());
			// summarize the board
			currentBoard.summarizeBoard();
			currentPlayerToPlay = currentBoard.getWinner().getSequenceNo();
			// reset the current board
			currentBoard = null;

		}
		summarizeRound();

	}

	/**
	 * Send the updated board to all the players.
	 * */
	private void boardUpdate(Board cB) throws IOException {
		
		/**
		 * Extract the information properly.
		 * */
		ArrayList<Move> m = new ArrayList<Move>();
		
		for(Process p:cB.getMoves().keySet()){
			m.add(cB.getMoves().get(p));
		}
		for (Process p : players) {
			p.boardUpdate(m);
		}
		
	}

	private void summarizeRound() {

		
		/*
		 * Iterate though each board, and distribute the score between 2 groups
		 * (kings men and commoners )
		 */
		int kingScore = 0, commonerScore = 0;
		for (Board b : boards) {
			if (kingsMen.contains(b.getWinner()))
				kingScore += b.getScore();
			else
				commonerScore += b.getScore();
		}

		// if kingsmen win, king gets the bid,kingsmen get half of bid and
		// commoners get nothing
		// if kingsmen lose, kings men dont get anything,commoners get the bid

		if (kingScore > this.roundParams.getMaxBid()) {
			// kings men won !
			scores.put(players[roundParams.getKing()], roundParams.getMaxBid() );
			for(Process p:kingsMen)
				scores.put(p, roundParams.getMaxBid()/2 );
			for(Process p:commoners)
				scores.put(p, 0);
			
		}else {
			//commoners won
			scores.put(players[roundParams.getKing()], 0 );
			for(Process p:kingsMen)
				scores.put(p, 0);
			for(Process p:commoners)
				scores.put(p, roundParams.getMaxBid());

		}

		System.out.println(scores);
	}

}
