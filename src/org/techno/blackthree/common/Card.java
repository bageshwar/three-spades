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
	
	
}
