/**
 * 
 */
package org.techno.blackthree.common;

import java.io.Serializable;

/**
 * 
 * This class would hold one move for one players.
 * This is a wrapper over the Card
 * 
 * @author bageshwp
 *
 */
public class Move implements Serializable{

	private Card card;

	public Card getCard() {
		return card;
	}

	public Move(Card c){
		card = c;
	}
	
	
}
