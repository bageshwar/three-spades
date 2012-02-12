/**
 * 
 */
package boardview.forms;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.techno.blackthree.common.Card;
import org.techno.blackthree.common.Move;

import boardview.Activator;

/**
 * @author Bageshwar
 * 
 */
public class DealDialog extends Dialog {

	
	
	private ArrayList<Move> deal;
	private Card myCard;
	
	private ArrayList<Card> myCards;
	
	/**
	 * @return the myCards
	 */
	public ArrayList<Card> getMyCards() {
		return myCards;
	}

	/**
	 * @param myCards the myCards to set
	 */
	public void setMyCards(ArrayList<Card> myCards) {
		this.myCards = myCards;
	}

	/**
	 * @return the myCard
	 */
	public Card getMyCard() {
		return myCard;
	}

	/**
	 * @return the deal
	 */
	public ArrayList<Move> getDeal() {
		return deal;
	}

	/**
	 * @param deal the deal to set
	 */
	public void setDeal(ArrayList<Move> deal) {
		this.deal = deal;
	}

	@SuppressWarnings("unchecked")
	public DealDialog(Shell parentShell, Object object) {
		super(parentShell);		
		deal=(ArrayList<Move>)object;
	}

	public static final String ID = "boardview.forms.DealDialog";

	
	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = (Composite) super.createDialogArea(parent);
		
		int dealSize=6;
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.verticalSpacing=30;		
		
		composite.setLayout(layout);
		
		Composite board = new Composite(composite,SWT.BORDER);
		//TableViewer board = new TableViewer(composite,SWT.BORDER);
		GridLayout layout1 = new GridLayout();
		layout1.numColumns =dealSize;
		layout1.horizontalSpacing=19;
		layout1.marginTop=5;
		layout1.marginBottom=5;
		board.setLayout(layout1);
				
		Composite myPack = new Composite(composite,SWT.BORDER);
		GridLayout layout2 = new GridLayout();
		layout2.numColumns =dealSize;		
		myPack.setLayout(layout2);
		
		for(Move m:deal){
			Card c = m.getCard();
			Label label = new Label(board,SWT.NATIVE);
			String n = "icons/75/"+c.getSuite().toString().toLowerCase()+"s-"+c.getFace().getFace().toLowerCase()+"-75.png";
			label.setImage(Activator.getImageDescriptor(n).createImage());
			//label.setEnabled(false);
			
		}
		
		
		
		final Label holder = new Label(board,SWT.NATIVE);
		String holderIcon = "icons/75/back-blue-75-2.png";
		holder.setImage(Activator.getImageDescriptor(holderIcon).createImage());
		
		//setting up blank place holders
		for(int i=0;i<dealSize-deal.size()-1;i++){
			Label label = new Label(board,SWT.NATIVE);
			String n = "icons/75/back-blue-75-2-d.png";
			label.setImage(Activator.getImageDescriptor(n).createImage());
			//label.setEnabled(false);
			
		}
		
		SelectionListener ls = new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
				
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				holder.setImage(((Button)arg0.getSource()).getImage());
				
				DealDialog.this.myCard = (Card) ((Button)arg0.getSource()).getData("card");
				
			}
			
		};
		
		for(Card c:myCards){
			Button button= new Button(myPack,SWT.NATIVE);
			String n = "icons/75/"+c.getSuite().toString().toLowerCase()+"s-"+c.getFace().getFace().toLowerCase()+"-75.png";
			button.setImage(Activator.getImageDescriptor(n).createImage());
			button.setData("card", c);
			button.addSelectionListener(ls);
		}

		
		return composite;

	}

	@Override
	protected void cancelPressed() {
	//cancel not allowed
		//super.cancelPressed();
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Play Deal...");
		newShell.setSize(600, 380);

	}

	@Override
	protected void okPressed() {
		
		
		super.okPressed();
	}
}
