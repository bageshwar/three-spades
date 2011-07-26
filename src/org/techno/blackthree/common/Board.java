/**
 * 
 */
package org.techno.blackthree.common;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class holds one set of move for each player.
 * @author bageshwp
 *
 */
public class Board implements Serializable {

	/**
	 * This should have the capability to return the net score
	 * based on partnerships.
	 * */
	HashMap<String,Move> board;
}
