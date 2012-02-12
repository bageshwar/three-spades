/**
 * 
 */
package boardview.common;

import org.techno.blackthree.common.Codes;
import org.techno.blackthree.common.event.GameEvent;
import org.techno.blackthree.common.event.GameEventListener;

/**
 * @author Bageshwar
 *
 */
public class GameEventListenerImpl implements GameEventListener {

	
	@Override
	public void consumeGameEvent(GameEvent gameEvent) {
		/**
		 * Handle various kinds of events here.
		 * */
		
		String code = gameEvent.getCode();
		
		if(code.equals(Codes.ACCEPT_HAND)){
			
			return;
		}

	}

}
