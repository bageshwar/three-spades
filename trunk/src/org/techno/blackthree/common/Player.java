/**
 * 
 */
package org.techno.blackthree.common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author bageshwp
 *
 */
public class Player implements Serializable {

	/**
	 * The set of cards this player has.
	 * */
	private ArrayList<Card> cards;
	private String name = ""+Math.random();
	
	
	
	/**
	 * @return the cards
	 */
	public ArrayList<Card> getCards() {
		return cards;
	}


	/**
	 * @param cards the cards to set
	 */
	public void setCards(ArrayList<Card> cards) {
		this.cards = cards;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
