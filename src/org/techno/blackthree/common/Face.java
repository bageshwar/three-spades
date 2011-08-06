/**
 * 
 */
package org.techno.blackthree.common;

/**
 * @author bageshwp
 *
 */
public enum Face {
 ACE ("A",10),
 THREE( "3"),
 FOUR ( "4"),
 FIVE ( "5",5),
 SIX ( "6"),
 SEVEN ( "7"),
 EIGHT ( "8"),
 NINE ( "9"),
 TEN ( "10",10),
 JACK ( "J",10),
 QUEEN ( "Q",10),
 KING ( "K",10);

	Face(String face){	 
		this(face,0);
	}
	
	Face(String face,int value){	 
		this.face = face;
		this.value = value;
	}
	
	private String face;
	
	public String getFace() {
		return face;
	}
	
	public int getValue() {
		return value;
	}
	

	private int value;

}