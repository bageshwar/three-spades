/**
 * 
 */
package org.techno.blackthree.common;

/**
 * @author bageshwp
 *
 */
public class InvalidDataStreamException extends Exception {

	private final static String  msg = "Invalid Stream Data: "; 
	public InvalidDataStreamException(String string) {
		super(msg + string);
	}

}
