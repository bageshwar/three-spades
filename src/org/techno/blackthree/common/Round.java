/**
 * 
 */
package org.techno.blackthree.common;

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
	Integer maxBid;
	
	Process king;
	
	/**	 
	 * The triumph as declared by the king
	 * */
	Suite triumph;
	
	/**
	 * The partner cards declared by the king
	 * */
	Card partnerCards[];
	
	public Round(){
		//partnerCards = new Card[3];
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
	public Process getKing() {
		return king;
	}

	/**
	 * @param king the king to set
	 */
	public void setKing(Process king) {
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
	 * @return the nextPlayerToBid. Returns -1, if the bid process needs to terminate.
	 */
	public int getNextPlayerToBid() {
		return nextPlayerToBid;
		
		
		
	}

	/**
	 * @param nextPlayerToBid the nextPlayerToBid to set
	 */
	public void setNextPlayerToBid(int nextPlayerToBid) {
		this.nextPlayerToBid = nextPlayerToBid;
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
	}
	
	
	

	
	
}
