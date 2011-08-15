/**
 * 
 */
package org.techno.blackthree.common;

import java.io.Serializable;
import java.util.LinkedHashMap;
import org.techno.blackthree.server.Process;

/**
 * This class holds one set of move for each player.
 * @author bageshwp
 *
 */
public class Board implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1175337023034931749L;
	/**
	 * This should have the capability to return the net score
	 * based on partnerships.
	 * */
	
	/**
	 * Preserves the order of moves
	 * */
	LinkedHashMap<Process,Move> moves;
	
	private Process winner;
	public LinkedHashMap<Process, Move> getMoves() {
		return moves;
	}

	public void setMoves(LinkedHashMap<Process, Move> moves) {
		this.moves = moves;
	}

	public Process getWinner() {
		return winner;
	}

	public void setWinner(Process winner) {
		this.winner = winner;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	private int score;
	
	public Board(){
		moves = new LinkedHashMap<Process,Move>();
	}
	
	
	
	
}
