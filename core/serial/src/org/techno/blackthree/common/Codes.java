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
	 * process the Game over summary.
	 * A hashmap is sent
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
	public static final String BOARD_UPDATE = "BOARD_UPDATE";
	
	/**
	 * Ask the client to place a bid.
	 * */
	public static final String BID = "BID";
	
	/**
	 * Flag tells the client of an updated bid.
	 * Consider using @link ROUND_PARAMETERS_UPDATE
	 * */
	public static final String BID_UPDATE = "BID_UPDATE";
	
	/**
	 * FLAG tells the client to accept a list of cards.
	 * */
	public static final String ACCEPT_HAND = "ACCEPT_HAND"; 
	
	/**
	 * Flag tells the client that the player list had been updated.
	 * */
	public static final String PLAYER_UPDATE = "PLAYER_UPDATE";
	
	/**
	 * Flag to send adhoc string messages to the client.
	 * */
	public static final String ADHOC_MESSAGE = "ADHOC_MESSAGE";
	
	/**
	 * Flag which tells the king to declare the triumph and 3 player cards.
	 * */
	public static final String KINGS_SPEECH = "KINGS_SPEECH";
	
	/**
	 * Flag which tells the client to accept the triumph and 3 player cards.
	 * */
	public static final String ROUND_PARAMETERS_UPDATE = "ROUND_PARAMETERS_UPDATE"; 
	
	/**
	 * Indicates that all players have joined the game
	 * */
	public static final String ALL_PLAYERS_JOINED = "ALL_PLAYERS_JOINED";
	
	/**
	 * Send a score update to the client.
	 * */
	public static final String SCORE_UPDATE = "SCORE_UPDATE";
	
	/**
	 * For Internal Use
	 * */
	public static final String INTERNAL_MESSAGE_PASSING="INTERNAL_MESSAGE_PASSING";
	
	/**
	 * Message to updated in the Log Console
	 * */
	public static final String LOG_MESSAGE="LOG_MESSAGE";
	
	/**
	 * Client/Server disconnected
	 * */
	public static final String DISCONNECTED = "DISCONNECTED";
	
	public static final String PLAYER_DISCONNECTED = "PLAYER_DISCONNECTED";
	public static final String PLAYER_CONNECTED = "PLAYER_CONNECTED";
	public static final String PLAYER_JOINED = "PLAYER_JOINED";
	
	/**
	 * Server Side Codes
	 * */
}
