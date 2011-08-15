/**
 * 
 */
package org.techno.blackthree.common;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

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
				}
			}
		}
		// now the kings men have been decided. time to set the commoners
		for (Process p : players) {
			if (!kingsMen.contains(p))
				commoners.add(p);
		}

	}

	/**
	 * Take the move from the board, process it. And set winner player and score
	 * for that board.
	 * */
	public void processBoard() {

		int boardScore = 0;
		int maxTriumph = 0;
		int maxCard = 0;
		int temp;
		Process leadingPlayer = null;
		Move m = null;
		for (Process p : currentBoard.getMoves().keySet()) {
			m = currentBoard.getMoves().get(p);
			boardScore += m.getCard().getValue();
			// this is the internal value of the card
			temp = m.getCard().getInternalValue();

			// need to check what was the initial suite.
			// if this does not match initial suite,
			// check if this is triumph, if so handle triumph,
			// else do not compare the internal value
		}

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
		Move move = null;
		for (int j = 0; j < 48 / len; j++) {
			for (int i = 0; i < len; i++) {
				// init board
				currentBoard = new Board();

				// add this board to the list of boards.
				boards.add(currentBoard);

				// iterate players.length no of times, access players by idx.
				move = players[currentPlayerToPlay].makeAMove();
				currentBoard.addMove(players[currentPlayerToPlay], move);
				// -->next player
				currentPlayerToPlay = (currentPlayerToPlay + 1) % len;

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// summarize the board
				currentBoard.summarize();
				// reset the current board
				currentBoard = null;
			}
		}
		summarizeRound();

	}

	private void summarizeRound() {
		// TODO Auto-generated method stub summarizeRound

	}

}
