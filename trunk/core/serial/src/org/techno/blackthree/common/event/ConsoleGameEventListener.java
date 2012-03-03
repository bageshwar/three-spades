/**
 * 
 */
package org.techno.blackthree.common.event;

import java.util.ArrayList;

import org.techno.blackthree.common.Codes;
import org.techno.blackthree.common.event.GameEvent;
import org.techno.blackthree.common.event.GameEventListener;


/**
 * @author Bageshwar
 *
 */
public class ConsoleGameEventListener  {

	private static ConsoleGameEventListener me;
	private ArrayList<GameEventListener> listeners;
	
	static {
		me =new ConsoleGameEventListener(); 
	}
	
	private ConsoleGameEventListener(){
		
	}
	
	public static void addGameEventListeners(ArrayList<GameEventListener> l) {
	me.listeners= l;	
	}
	
	public static void addListener(GameEventListener l){
		me.listeners.add(l);
	}
	
	public static  void debug(String msg) {
		System.out.println(msg);
		me.fireGameEvent(new GameEvent(Codes.LOG_MESSAGE,msg));
		
	}
	
	public static void castMessage(String msg){
		// need to cast a msg to all connected players.
	}
	/**
	 * For purposes other than regular debug.
	 * to notify the UI of an action.
	 * */	
	public synchronized static void fireOtherGameEvent(GameEvent ge){
		me.fireGameEvent(ge);
	}

	private void fireGameEvent(GameEvent gameEvent) {
		for(GameEventListener gel: listeners ){
			gel.consumeGameEvent(gameEvent);			
		}
		
	}
	
	

}
