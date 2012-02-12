package boardview.common;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.techno.blackthree.common.Card;
import org.techno.blackthree.common.Face;
import org.techno.blackthree.common.Suite;

/**
 * The content provider class is responsible for providing objects to the view.
 * It can wrap existing objects in adapters or simply return objects as-is.
 * These objects may be sensitive to the current input of the view, or ignore it
 * and always show the same content (like Task List, for example).
 * 
 * @author Bageshwar
 */
 
public class ViewContentProvider implements IStructuredContentProvider {
	Card[] cards ;

	
	public ViewContentProvider(Card[] c) {
		cards =c;
	}
	
	/**
	 * @return the cards
	 */
	public Card[] getCards() {
		return cards;
	}

	/**
	 * @param cards the cards to set
	 */
	public void setCards(Card[] cards) {
		this.cards = cards;
	}

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object parent) {

		return cards;
		// return null;
	}
}
