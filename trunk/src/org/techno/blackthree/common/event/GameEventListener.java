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

	public void consumeGameEvent(GameEvent gameEvent);
}
