/**
 * 
 */
package boardview.common;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.techno.blackthree.common.Card;
import org.techno.blackthree.common.Move;

import boardview.Activator;

/**
 * @author Bageshwar
 * 
 */
public class BoardDesigner {

	CardSelectionListener selectionListener;

	/**
	 * @return the selectionListener
	 */
	public CardSelectionListener getSelectionListener() {
		return selectionListener;
	}

	/**
	 * @param selectionListener
	 *            the selectionListener to set
	 */
	public void setSelectionListener(CardSelectionListener selectionListener) {
		this.selectionListener = selectionListener;
	}

	private ArrayList<Move> deal;
	// private Card myCard;

	private ArrayList<Card> myCards;

	// private RoundParameters roundParams;

	/**
	 * @return the myCards
	 */
	public ArrayList<Card> getMyCards() {
		return myCards;
	}

	/**
	 * @param myCards
	 *            the myCards to set
	 */
	public void setMyCards(ArrayList<Card> myCards) {
		this.myCards = myCards;
	}

	/**
	 * @return the deal
	 */
	public ArrayList<Move> getDeal() {
		return deal;
	}

	/**
	 * @param deal
	 *            the deal to set
	 */
	public void setDeal(ArrayList<Move> deal) {
		this.deal = deal;
	}

	public BoardDesigner() {
		// TODO Auto-generated constructor stub
	}

	/*public static void modifyDeal(Composite deal,ArrayList<Move> moves, int dealSize, String size){
		
		for(int i=0;i<moves.size();i++){
			Move m = moves.get(i);
			Card c = m.getCard();
			Label label = ((Label)deal.getChildren()[i]);
			label.setToolTipText(m.getPlayer());
			String n = "icons/" + size + "/" + c.getSuite().toString().toLowerCase() + "s-"
					+ c.getFace().getFace().toLowerCase() + "-75.png";
			label.setImage(Activator.getImageDescriptor(n).createImage());		`
		}
		for(int i=moves.size();i<dealSize;i++){			
			Label label = ((Label)deal.getChildren()[i]);			
			String n = "icons/" + size + "/" + c.getSuite().toString().toLowerCase() + "s-"
					+ c.getFace().getFace().toLowerCase() + "-75.png";
			label.setImage(Activator.getImageDescriptor(n).createImage());
		}
	}*/
	
	/*public static void modifyHand(Composite hand,ArrayList<Card> cards, int handSize, String size){
		
		for(int i=0;i<handSize;i++){			
			Card c = cards.get(i);
			Label label = ((Label)hand.getChildren()[i]);
			//label.setToolTipText(m.getPlayer());
			String n = "icons/" + size + "/" + c.getSuite().toString().toLowerCase() + "s-"
					+ c.getFace().getFace().toLowerCase() + "-75.png";
			label.setImage(Activator.getImageDescriptor(n).createImage());
		
		}
	}*/
	
	/**
	 * Designs the Deal Pack i.e. In Game.
	 * */
	public static void arrangeDeal(Composite deal, ArrayList<Move> moves, int dealSize, String size) {
		
		for (int i=0;i<moves.size();i++) {
			Move m = moves.get(i);
			Card c = m.getCard();
			Label label = (Label) deal.getChildren()[i];
			label.setToolTipText(m.getPlayer());
			String n = "icons/" + size + "/" + c.getSuite().toString().toLowerCase() + "s-"
					+ c.getFace().getFace().toLowerCase() + "-75.png";
			label.setImage(Activator.getImageDescriptor(n).createImage());
			
		}

		/*
		 * final Label holder = new Label(deal,SWT.NATIVE);
		 * holder.setToolTipText("Your turn here..."); String holderIcon =
		 * "icons/75/back-blue-75-2.png";
		 * holder.setImage(Activator.getImageDescriptor
		 * (holderIcon).createImage());
		 */

		// setting up blank place holders
		for (int i = moves.size(); i < dealSize  ; i++) {
			Label label = (Label) deal.getChildren()[i];
			String n = "icons/" + size + "/back-blue-75-2-d.png";
			label.setImage(Activator.getImageDescriptor(n).createImage());
		}
	}

	/**
	 * This method designs the Hand. ie. Player's Cards
	 * */
	public static void arrangeHand(Composite hand, ArrayList<Card> myCards, int total, String size) {
		for (int i=0;i<myCards.size();i++) {
			Card c = myCards.get(i);			
			Label label = (Label) hand.getChildren()[i];
			String n = "icons/" + size + "/" + c.getSuite().toString().toLowerCase() + "s-"
					+ c.getFace().getFace().toLowerCase() + "-75.png";
			label.setImage(Activator.getImageDescriptor(n).createImage());
			label.setData("card", c);
		}

		// setting up blank place holders
		for (int i = myCards.size(); i < total ; i++) {
			Label label = (Label) hand.getChildren()[i];
			String n = "icons/" + size + "/back-red-75-2-d.png";
			label.setImage(Activator.getImageDescriptor(n).createImage());
		}
	}

	/**
	 * This method lays down the components for displaying the board.
	 * @param logger 
	 * */
	public static Object[] createBoardSkeleton(Composite parent, Text logger, SashForm form, Composite deal, Composite hand,
			int dealSize, int handSize) {
		GridLayout layout4 = new GridLayout();
		layout4.numColumns = 3;
		//layout4.verticalSpacing = 30;
		
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		form = new SashForm(parent,SWT.HORIZONTAL);//toolkit.create(parent);
		//form.setText("Hello, Eclipse Forms");
		
		form.setLayout(new FillLayout());		
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.verticalSpacing = 30;
		
		Composite board = toolkit.createComposite(form);
		board.setLayout(layout);
		
		
		//Sash s = new Sash(form.,SWT.VERTICAL);
		
		
		logger = toolkit.createText(form,"",SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL );
		//sf.setText("FORM");
		logger.setEditable(false);
		
		deal = toolkit.createComposite(board, SWT.BORDER);
		// TableViewer board = new TableViewer(composite,SWT.BORDER);
		GridLayout layout1 = new GridLayout();
		layout1.numColumns = dealSize;
		//layout1.horizontalSpacing = 19;
		//layout1.marginTop = 5;
		//layout1.marginBottom = 5;
		deal.setLayout(layout1);

		hand = toolkit.createComposite(board, SWT.BORDER);
		GridLayout layout2 = new GridLayout();
		layout2.numColumns = handSize;
		hand.setLayout(layout2);
		
		for(int i=0;i<dealSize;i++)
			new Label(deal,SWT.NONE);
		
		for(int i=0;i<handSize;i++)
			new Label(hand,SWT.NONE);
		
		return new Object[] {form,logger, deal, hand };
	}

	public Composite getBoard(Composite composite) {

		int dealSize = 8;

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.verticalSpacing = 30;

		composite.setLayout(layout);

		Composite board = new Composite(composite, SWT.BORDER);
		// TableViewer board = new TableViewer(composite,SWT.BORDER);
		GridLayout layout1 = new GridLayout();
		layout1.numColumns = dealSize;
		layout1.horizontalSpacing = 19;
		layout1.marginTop = 5;
		layout1.marginBottom = 5;
		board.setLayout(layout1);

		Composite myPack = new Composite(composite, SWT.BORDER);
		GridLayout layout2 = new GridLayout();
		layout2.numColumns = dealSize;
		myPack.setLayout(layout2);

		for (Move m : deal) {
			Card c = m.getCard();
			Label label = new Label(board, SWT.NATIVE);
			label.setToolTipText(m.getPlayer());
			String n = "icons/75/" + c.getSuite().toString().toLowerCase() + "s-" + c.getFace().getFace().toLowerCase()
					+ "-75.png";
			label.setImage(Activator.getImageDescriptor(n).createImage());
			// label.setEnabled(false);

		}

		final Label holder = new Label(board, SWT.NATIVE);
		holder.setToolTipText("Your turn here...");
		String holderIcon = "icons/75/back-blue-75-2.png";
		holder.setImage(Activator.getImageDescriptor(holderIcon).createImage());
		//selectionListener.setHolder(holder);

		// setting up blank place holders
		for (int i = 0; i < dealSize - deal.size() - 1; i++) {
			Label label = new Label(board, SWT.NATIVE);
			String n = "icons/75/back-blue-75-2-d.png";
			label.setImage(Activator.getImageDescriptor(n).createImage());
			// label.setEnabled(false);

		}

		for (Card c : myCards) {
			Button button = new Button(myPack, SWT.NATIVE);
			String n = "icons/75/" + c.getSuite().toString().toLowerCase() + "s-" + c.getFace().getFace().toLowerCase()
					+ "-75.png";
			button.setImage(Activator.getImageDescriptor(n).createImage());
			button.setData("card", c);
			button.addSelectionListener(selectionListener);
		}

		return composite;

	}

}
