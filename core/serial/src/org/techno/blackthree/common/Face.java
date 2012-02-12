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
	

	public int getInternalValue(){
		if(face.equals("10"))
			return 10;
		else if(face.equals("J"))
			return 11;
		else if(face.equals("Q"))
			return 12;
		else if(face.equals("K"))
			return 13;
		else if(face.equals("A"))
			return 14;
		//if none of these, then its a pure number, parse the rep
		else return Integer.parseInt(face);
	}
	private int value;

}
