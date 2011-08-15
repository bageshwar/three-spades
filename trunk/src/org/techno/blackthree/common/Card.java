/**
 * 
 */
package org.techno.blackthree.common;

import java.io.Serializable;

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
	
	public int getValue(){
		if(this.suite.equals(Suite.SPADE) && this.face.equals(Face.THREE))
			return 30;
		return face.getValue();
	}
	
	public int getInternalValue(){
		return internalValue;
	}
	
	@Override
	public String toString() {
	
		return suite.toString()+"-" + face.getFace();
	}
}
