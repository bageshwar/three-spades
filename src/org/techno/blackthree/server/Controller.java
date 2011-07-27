/**
 * 
 */
package org.techno.blackthree.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.techno.blackthree.common.Card;
import org.techno.blackthree.common.Face;
import org.techno.blackthree.common.InvalidDataStreamException;
import org.techno.blackthree.common.Player;
import org.techno.blackthree.common.Suite;

/**
 * The server sends the control to this class, which does all the syncing
 * 
 * @author bageshwp
 * 
 */
public class Controller implements Runnable {

	private int size;

	private boolean tired = false;
	
	public boolean isTired() {
		return tired;
	}

	public void setTired(boolean tired) {
		this.tired = tired;
	}

	/**
	 * An array, since number of players is fixed.
	 * */
	private Process[] players;

	public int getSize() {
		return size;
	}

	public void addPlayer(final int index, final Socket s) {
		new Thread(new Runnable() {

			@Override
			public void run() {

				System.out.println("Player #"+index);
				// create a process
				Process p = null;
				try {

					// init the streams and get the player info
					p = new Process(s);
					
					
					//send all the initial player's update to this player 					
					p.sendAllPlayersUpdate(players);
					p.sendMessage("Waiting for " + (size - index-1) + "/" + size + " players ");
					
					Controller.this.sendPlayerUpdate(p.getPlayer().getName());
					Controller.this.sendMessage("Waiting for " + (size - index-1) + "/" + size + " players ");

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidDataStreamException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//adding this player to the list if all goes well.
				if (p != null) {
					players[index] = p;
				}
			}
		}).start();

	}

	protected void sendPlayerUpdate( final String name) {


		for (Process p : players) {
			
			//to filter out null players and the currently joined player
			if (p == null )
				continue;
			
			try {
				p.sendPlayerUpdate(name);
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	public Controller(int size) {

		this.size = size;
		players = new Process[size];
	}

	/**
	 * 
	 * */
	@Override
	public void run() {
		sendMessage("All " + size + " players have joined.Starting the game...");
		//while(!isTired()){
		this.distributeDeal();
		
		//}
	}
	
		
	

	/**
	 * Broadcast a message to all the players.
	 * */
	synchronized private void sendMessage(final String msg) {

		for (Process p : players) {
			if (p == null)
				continue;
			try {
				p.sendMessage(msg);
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
	
	private void distributeDeal(){
		
		HashMap<Process,ArrayList<Card>> pack= new HashMap<Process,ArrayList<Card>>();
		
		Card[] freshPack = getFreshPack();
		
		int random = getRandomNumber();
		
		ArrayList<Card> deal = null;
		
		for(int i=0;i<freshPack.length/size;i++){
			for(int j=0;j<size;j++){
				while(freshPack[random]==null)
					random = getRandomNumber();
				
				deal = pack.get(players[j]);
				if(deal == null)
					deal = new ArrayList<Card>();
				deal.add(freshPack[random]);				
				pack.put(players[j],deal );
				
				freshPack[random]=null;
			}
		}
		
		System.out.println(pack);
		
		//the deal is ready.. please distribute it.
		
		for (Process p : players) {
			if (p == null)
				continue;
			try {
				p.distributeDeal(pack.get(p));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
	
	private int getRandomNumber(){
		return (int)(Math.random()*48);
	}
	
	private Card[] getFreshPack(){
		
		ArrayList<Card> pack = new ArrayList<Card>();
		
		for(Face face:Face.values()){
			for(Suite suite:Suite.values()){
			pack.add(new Card(suite, face));	
			}
		}
		
		return pack.toArray(new Card[]{});
		
	}
}
