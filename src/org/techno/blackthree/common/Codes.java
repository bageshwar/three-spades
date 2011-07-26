/**
 * 
 */
package org.techno.blackthree.common;

/**
 * Contains the list of codes to be used by the client-server
 * @author bageshwp
 *
 */
public class Codes {

	//public static final String  = "";
	
	/**
	 * When the Game is over, flag to be sent to client to
	 * process the Game over summary
	 * */
	public static final String GAME_OVER = "GAME_OVER";
	
	/**
	 * When the Round is over, flag to be sent to client to
	 * process the Round over summary
	 * */
	public static final String ROUND_OVER = "ROUND_OVER";
	
	/**
	 * Flag to tell the client to move.
	 * */
	public static final String MOVE = "MOVE";
	
	/**
	 * Invalid Move
	 * */
	public static final String INVALID_MOVE = "INVALID_MOVE";
	
	/**
	 * Status 200. Connection Successful.
	 * */
	public static final String OK = "OK";
	
	/**
	 * Flag, which tells the client that the connection was successful.
	 * Provide the player details.
	 * */
	public static final String GIVE_PLAYER = "GIVE_PLAYER";
	
	/**
	 * Flag tells the client of an updated board.
	 * */
	public static final String ROUND_UPDATE = "ROUND_UPDATE";
	
	/**
	 * Ask the client to place a bid.
	 * */
	public static final String BID = "BID";
	
	/**
	 * Flag tells the client of an updated bid.
	 * */
	public static final String BID_UPDATE = "BID_UPDATE";
	
	/**
	 * FLAG tells the client to accept a list of cards.
	 * */
	public static final String ACCEPT_HAND = "ACCEPT_HAND"; 
}
