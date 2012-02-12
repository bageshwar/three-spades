/**
 * 
 */
package org.techno.blackthree.common;

import java.io.Serializable;

/**
 * 
 * This class would hold one move for one players. This is a wrapper over the
 * Card
 * 
 * @author bageshwp
 * 
 */
public class Move implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1950757898598108115L;
	private Card card;

	private String player;

	/**
	 * @return the player
	 */
	public String getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(String player) {
		this.player = player;
	}

	public Card getCard() {
		return card;
	}

	public Move(Card c) {
		card = c;
	}

	@Override
	public String toString() {
		return card.toString();
	}

}
