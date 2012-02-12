package org.techno.blackthree.common;

import java.io.Serializable;
import java.util.Arrays;

public class RoundParameters implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5955350263827869627L;

	/**
	 * The maximum bid for this round
	 * */
	protected Integer maxBid = 0;
	
	protected int king;
	
	private String kingPlayer = null;
	
	public String getKingPlayer() {
		return kingPlayer;
	}

	public void setKingPlayer(String kingPlayer) {
		this.kingPlayer = kingPlayer;
	}

	/**	 
	 * The triumph as declared by the king
	 * */
	protected Suite triumph;
	
	/**
	 * The partner cards declared by the king
	 * */
	protected Card partnerCards[];

	
	public RoundParameters(int size){
		//since only 3 partner cards can be set.		
		partnerCards = new Card[(size/2)-1];
		
	}
	
	/**
	 * Empty constructor
	 * */
	public RoundParameters(){}
	
	public Integer getMaxBid() {
		return maxBid;
	}

	public void setMaxBid(Integer maxBid) {
		this.maxBid = maxBid;
	}

	public int getKing() {
		return king;
	}

	public void setKing(int king) {
		this.king = king;
	}

	public Suite getTriumph() {
		return triumph;
	}

	public void setTriumph(Suite triumph) {
		this.triumph = triumph;
	}

	public Card[] getPartnerCards() {
		return partnerCards;
	}

	public void setPartnerCards(Card[] partnerCards) {
		this.partnerCards = partnerCards;
	}

	/**
	 * Return the Round Parameters
	 * */
	public RoundParameters getThis(){
		return this;
	}
	
	@Override
	public String toString() {
	
		return kingPlayer+" made a Bid of "+maxBid+", Triumph is "+triumph+" and Partner Cards are "+Arrays.toString(partnerCards);
	}
	
}
