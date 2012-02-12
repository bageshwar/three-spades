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
public class Card implements Serializable{

	private int internalValue = 0;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5145549398423800382L;
	Suite suite;
	Face face;
	public Card(Suite suite, Face face) {
		
		this.suite = suite;
		this.face = face;
		//to avoid calculating this again
		internalValue = face.getInternalValue();
	}
	public Suite getSuite() {
		return suite;
	}
	public Face getFace() {
		return face;
	}
	
	/**
	 * The point value of the card. Used to calculate the scores.
	 * */
	public int getValue(){
		if(this.suite.equals(Suite.SPADE) && this.face.equals(Face.THREE))
			return 30;
		return face.getValue();
	}
	
	/**
	 * The internal value of the card. Used to decide the winner card.
	 * */
	public int getInternalValue(){
		return internalValue;
	}
	
	@Override
	public String toString() {
	
		return suite.toString()+"-" + face.getFace();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((face == null) ? 0 : face.hashCode());
		result = prime * result + ((suite == null) ? 0 : suite.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (face == null) {
			if (other.face != null)
				return false;
		} else if (!face.equals(other.face))
			return false;
		if (suite == null) {
			if (other.suite != null)
				return false;
		} else if (!suite.equals(other.suite))
			return false;
		return true;
	}
	
	/**
	 * Returns a Fresh Pack
	 * */
	public static Card[] getFreshPack() {

		ArrayList<Card> pack = new ArrayList<Card>();

		for (Face face : Face.values()) {
			for (Suite suite : Suite.values()) {
				pack.add(new Card(suite, face));
			}
		}

		return pack.toArray(new Card[] {});

	}
	public static Card[][] getFreshPackPerSuite(){
		Card[][] pack = new Card[Suite.values().length][Face.values().length];

		int i=0,j=0;
		for (Suite suite : Suite.values()) {
		for (Face face : Face.values()) {			
				pack[i][j++]=(new Card(suite, face));
			}
			j=0;
			i++;
		}

		return pack;

	}
	
}
