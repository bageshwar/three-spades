/**
 * 
 */
package org.techno.blackthree.common;

import org.techno.blackthree.server.Process;

/**
 * 
 * This bean holds the data for a single round.
 * @author bageshwp
 *
 */
public class Round extends RoundParameters {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1049786175808878897L;

	/**
	 * Transient because its not required to be serialized.
	 * */
	transient Process[] players;
	
	/**
	 * The Next player to bid
	 * */
	int nextPlayerToBid;
	
	/**
	 * These players passed the bid to the next player.
	 * When this would equal players#-1, we would have the king
	 * */
	private int playersPassed=0;
	
	/**
	 * The player with whom the round will be started.
	 * */
	int initialPlayer;
	
	/**
	 * The next player to play his card
	 * */
	int currentPlayerToPlay;
	
	
	
	/**
	 * @param _players Players
	 * @param _initialPlayer Index of initial Player for this round
	 * */
	public Round(Process[] _players,int _initialPlayer ){
		players = _players;
		initialPlayer = _initialPlayer;
		
		
	}

	/**
	 * @return the players
	 */
	public Process[] getPlayers() {
		return players;
	}

	/**
	 * @param players the players to set
	 */
	public void setPlayers(Process[] players) {
		this.players = players;
	}
		
	
	/**
	 * @param response The response of the player. {-1:passed,valid int:value}
	 * @return the nextPlayerToBid. Returns -1, if the bid process needs to terminate.
	 */
	public int getNextPlayerToBid(int response) {
		//return nextPlayerToBid;
				
		
		//if this is the first bid, send the initial player's id to continue !!
		if(response==0)
			return this.getInitialPlayer();
		
		
		
		//if player passed
		if(response==-1){
			playersPassed++;
			System.out.println(players[nextPlayerToBid]+" passed");
		}else 
			System.out.println(players[nextPlayerToBid]+" bid "+response);
		
		//if all players passed.
		if(playersPassed==players.length-1){
			System.out.println("All players passed.["+ players[king].toString()+" >> "+
					maxBid+"]: start the game");
			return -1;
		}
		

		/**
		 * If this is a legitimate bid
		 * check max bid, if yes, set king as current player, 
		 * and reset players passed. 
		 * */
		if(maxBid<response){
			maxBid = response;
			king = nextPlayerToBid ;
			playersPassed=0;
			this.setKingPlayer(players[king].getPlayer().getName());
			System.out.println("We have a new King: "+players[king].toString());
		}
		
		//if this is the max bid
		if(maxBid==250){
			System.out.println("Found Ultimate Max bid["+ players[king].toString()+"]: start the game");
			return -1;
		}
		
		nextPlayerToBid = (nextPlayerToBid+1)%players.length;		
		return nextPlayerToBid;
		
	}
	

	/**
	 * @return the currentPlayerToPlay
	 */
	public int getCurrentPlayerToPlay() {
		return currentPlayerToPlay;
	}

	/**
	 * @param currentPlayerToPlay the currentPlayerToPlay to set
	 */
	public void setCurrentPlayerToPlay(int currentPlayerToPlay) {
		this.currentPlayerToPlay = currentPlayerToPlay;
	}

	/**
	 * @return the initialPlayer
	 */
	public int getInitialPlayer() {
		return initialPlayer;
	}

	/**
	 * @param initialPlayer the initialPlayer to set
	 */
	public void setInitialPlayer(int initialPlayer) {
		this.initialPlayer = initialPlayer;
		this.setKing(initialPlayer);
		//this.setNextPlayerToBid(initialPlayer);
	}


	public RoundParameters getRoundParameters(){
		return super.getThis();
	}
}
