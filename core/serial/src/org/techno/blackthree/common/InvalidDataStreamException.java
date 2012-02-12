/**
 * 
 */
package org.techno.blackthree.common;

/**
 * @author bageshwp
 *
 */
public class InvalidDataStreamException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4533583179009813372L;
	private final static String  msg = "Invalid Stream Data: "; 
	public InvalidDataStreamException(String string) {
		super(msg + string);
	}

}
