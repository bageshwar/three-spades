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

	/**
	 * 
	 */
	private static final long serialVersionUID = 5145549398423800382L;
	Suite suite;
	Face face;
	public Card(Suite suite, Face face) {
		
		this.suite = suite;
		this.face = face;
	}
	public Suite getSuite() {
		return suite;
	}
	public Face getFace() {
		return face;
	}
	
	@Override
	public String toString() {
	
		return suite.toString()+"-" + face.getFace();
	}
}
