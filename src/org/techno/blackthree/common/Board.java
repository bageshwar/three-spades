/**
 * 
 */
package org.techno.blackthree.common;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

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
	
	private RoundParameters params = null;
	
	private int score;
	
	/**
	 * Usual getter for the field.
	 * */
	public LinkedHashMap<Process, Move> getMoves() {
		return moves;
	}

	
	/**
	 * Usual Setter for the Field.
	 * */
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

	
	
	public Board(RoundParameters params){
		moves = new LinkedHashMap<Process,Move>();
		this.params = params;
	}
	
	/**
	 * Add a move sequence to this board.
	 * Since the board is backed by the LinkedHashMap, 
	 * it will preserve the order of moves.
	 * @param p The player which has moved 
	 * @param m The move.
	 * */
	public void addMove(Process p,Move m){		
		moves.put(p, m);
	}

	/**
	 * Iterate through the moves, and 
	 * */
	public void summarize() {

		
		Set<Process> set =moves.keySet();
		Iterator<Process> itr = set.iterator(); 
		Process firstPlayer = itr.next();
		Move firstPlayerMove = moves.get(firstPlayer);
		score+= firstPlayerMove.getCard().getValue();
		
		Process p = null;
		Move m = null;
		int maxTriumphWeight = 0;
		int maxSuiteWeight = firstPlayerMove.getCard().getInternalValue();
		Process maxTriumphPlayer = null;
		Process maxSuitePlayer = firstPlayer;
		while(itr.hasNext()){
			p = itr.next();
			m = moves.get(p);
			score+=m.getCard().getValue();
			/**
			 * Logic :
			 * The firstPlayer's suite is the master suite.
			 * Next Player's suite should either be the triumph, or the master suite.
			 * */
			if(m.getCard().getSuite().equals(firstPlayerMove.getCard().getSuite())){
				//same suite
				//System.out.println("Suite "+m+" Internal: "+m.getCard().getInternalValue()+" W:"+maxTriumphWeight );
				if(m.getCard().getInternalValue()>maxSuiteWeight ){
					maxSuiteWeight = m.getCard().getInternalValue();
					maxSuitePlayer = p;
					//System.out.println("Max suite "+maxSuiteWeight);
				}
			}else if(m.getCard().getSuite().equals(params.getTriumph())){
				//triumph
				//System.out.println("Trump "+m+" Internal: "+m.getCard().getInternalValue()+" W:"+maxTriumphWeight );
				if(m.getCard().getInternalValue()>maxTriumphWeight ){
					maxTriumphWeight = m.getCard().getInternalValue();
					maxTriumphPlayer = p;					
				}
			}
			
		}
		if(maxTriumphPlayer!=null){
			//the winner is the triumph player
			this.winner = maxTriumphPlayer;
		}else {
			this.winner = (maxSuitePlayer==null)?firstPlayer:maxSuitePlayer;
		}
		
		System.out.println("\u265b is "+winner+" > "+moves.get(winner)+" and Total Score="+score);
		
	}
	
	
}
