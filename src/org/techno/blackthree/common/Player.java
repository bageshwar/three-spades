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
	 * 
	 */
	private static final long serialVersionUID = 4468181617287557415L;
	/**
	 * The set of cards this player has.
	 * */
	private ArrayList<Card> cards;
	private String name ;
	
	
	
	public Player(String name) {
		this(new ArrayList<Card>(),name);
	}


	public Player(ArrayList<Card> cards, String name) {
		
		this.cards = cards;
		this.name = name;
	}


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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {

		return this.getName();
	}
}
