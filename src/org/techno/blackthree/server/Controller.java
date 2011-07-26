/**
 * 
 */
package org.techno.blackthree.server;

import java.io.IOException;
import java.net.Socket;

import org.techno.blackthree.common.InvalidDataStreamException;

/**
 * The server sends the control to this class, which does all the syncing
 * 
 * @author bageshwp
 * 
 */
public class Controller implements Runnable {

	private int size;
	

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

				// create a process
				Process p = null;
				try {
					
					//init the streams and get the player info
					p = new Process(s);
					Controller.this.sendMessage(p.getPlayer().getName()+ " has joined.");
					Controller.this.sendMessage(" Waiting for "+(size-index)+"/"+size+" players ");
				
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
				if (p != null) {
					players[index] = p;
				}
			}
		}).start();

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
	}


	/**
	 * Broadcast a message to all the players.
	 * */
	synchronized private void sendMessage(final String msg) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (Process p : players) {
					try {
						p.sendMessage(msg);
					} catch (IOException e) {

						e.printStackTrace();
					}
				}

			}
		}).start();

	}
}
