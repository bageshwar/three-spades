/**
 * 
 */
package boardview.common;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.techno.blackthree.common.Card;

/**
 * @author Bageshwar
 * 
 */
public class CardSelectionListener implements SelectionListener {

	private Card myCard;

	private Label holder;

	/**
	 * @return the holder
	 */
	public Label getHolder() {
		return holder;
	}

	/**
	 * @param holder
	 *            the holder to set
	 */
	public void setHolder(Label holder) {
		this.holder = holder;
	}

	/**
	 * @return the myCard
	 */
	public Card getMyCard() {
		return myCard;
	}

	public CardSelectionListener() {		
		
	}

	/**
	 * @param myCard
	 *            the myCard to set
	 */
	public void setMyCard(Card myCard) {
		this.myCard = myCard;
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {

	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		holder.setImage(((Button) arg0.getSource()).getImage());

		myCard = (Card) ((Button) arg0.getSource()).getData("card");

	}

}
