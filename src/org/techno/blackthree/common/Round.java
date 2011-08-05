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
public class Round {

	
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
	 * The maximum bid for this round
	 * */
	Integer maxBid = 0;
	
	int king;
	
	/**	 
	 * The triumph as declared by the king
	 * */
	Suite triumph;
	
	/**
	 * The partner cards declared by the king
	 * */
	Card partnerCards[];
	
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
	 * @return the maxBid
	 */
	public Integer getMaxBid() {
		return maxBid;
	}

	/**
	 * @param maxBid the maxBid to set
	 */
	public void setMaxBid(Integer maxBid) {
		this.maxBid = maxBid;
	}

	/**
	 * @return the king
	 */
	public int getKing() {
		return king;
	}

	/**
	 * @param king the king to set
	 */
	public void setKing(int king) {
		this.king = king;
	}

	/**
	 * @return the triumph
	 */
	public Suite getTriumph() {
		return triumph;
	}

	/**
	 * @param triumph the triumph to set
	 */
	public void setTriumph(Suite triumph) {
		this.triumph = triumph;
	}

	/**
	 * @return the partnerCards
	 */
	public Card[] getPartnerCards() {
		return partnerCards;
	}

	/**
	 * @param partnerCards the partnerCards to set
	 */
	public void setPartnerCards(Card[] partnerCards) {
		this.partnerCards = partnerCards;
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
			System.out.println("We have a new King: "+players[king].toString());
		}
		
		//if this is the max bid
		if(maxBid==250){
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
	
	
	
/**
 * Set the initial player,and players
 * */
	
	
}
