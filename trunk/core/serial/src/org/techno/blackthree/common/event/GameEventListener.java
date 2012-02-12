/**
 * 
 */
package org.techno.blackthree.common.event;

import java.util.EventListener;

/**
 * @author Bageshwar
 *
 */
public interface GameEventListener extends EventListener {

	/**
	 * If the event requires some data to be returned,
	 * that would be done by setting the response inside the gameEvent.
	 * */
	public void consumeGameEvent(GameEvent gameEvent);
}
