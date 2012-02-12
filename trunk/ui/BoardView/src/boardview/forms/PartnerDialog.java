/**
 * 
 */
package boardview.forms;

import java.util.HashMap;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.techno.blackthree.common.Card;
import org.techno.blackthree.common.Suite;

import boardview.Activator;

/**
 * @author Bageshwar
 * 
 */
public class PartnerDialog extends Dialog {

	public static final String ID = "boardview.forms.PartnerDialog";

	private Card[] partnerCards = new Card[3];
	
	private Suite triumph;

	/**
	 * @return the triumph
	 */
	public Suite getTriumph() {
		return triumph;
	}

	private CheckboxTreeViewer viewer;

	public PartnerDialog(Shell parentShell) {
		super(parentShell);

	}

	public Card[] getPartnerCards() {
		return partnerCards;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = (Composite) super.createDialogArea(parent);

		Label label2 = new Label(composite, SWT.NONE);
		label2.setText("Select Partner Cards ");
		viewer = new CheckboxTreeViewer(composite);
		viewer.setContentProvider(new BackupFilesContentProvider());
		viewer.setLabelProvider(new BackupFilesLabelProvider());
		viewer.addCheckStateListener(new ICheckStateListener() {

			@Override
			public void checkStateChanged(CheckStateChangedEvent arg0) {

			}
		});

		viewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent node) {
				// need to check if this is a suite, in which case, need to
				// expand the node.
				if (((TreeSelection) node.getSelection()).getFirstElement() instanceof Suite) {
					// Disabled the Expand-on-Double-Click for the time being.

					/*
					 * ((CheckboxTreeViewer)
					 * node.getViewer()).expandToLevel(((TreeSelection)
					 * node.getSelection()) .getFirstElement(), 10);
					 */
				}

			}
		});

		viewer.setInput("T");
		GridData data = new GridData(GridData.FILL_BOTH);
		viewer.getControl().setLayoutData(data);
		
		return composite;

	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Select Partner Cards and Triumph");
		newShell.setBounds(100, 100, 300, 500);

	}

	@Override
	protected void cancelPressed() {
		//consume cancel
		//super.cancelPressed();
	}
	
	@Override
	protected void okPressed() {

		// need to perform the validation here

		//partnerCards = (Card[]) viewer.getCheckedElements();
		Object[]  obj =  viewer.getCheckedElements();

		triumph = null;
		boolean flag=true;
		String msg ="";
		int i=0;
		if (obj.length == 4) {
			
			// validate for one triumph card, and 3 other partner cards.
			for(Object card: obj){
				//one and ONLY one should be triumph
				
				if(card instanceof Suite){
					if(triumph==null){
						triumph = (Suite) card;
					}else {
						//MORE THAN ONE TRIUMPH
						flag=false;
						msg+="\r\nYou cannot select more than 1 triumph card";
						break;
					}
				}else {
					if(i>=3){
						flag=false;
						msg+="\r\nYou have to select at least 1 Triumph card";
						break;
					}else
						
					partnerCards[i++] = (Card) card;
				}
				
			}
			
			
		} else {
			msg+="\r\nYou have to select 1 Triumph card, and 3 Partner cards.";
			flag=false;			
		}
		if(flag){
			
			super.okPressed();
		}else {
			//show msg
			//MessageDialog md = new MessageDialog(null,"t",null,"m",0,null,0);
			//md.open();
			Status status = new Status(IStatus.ERROR, "Black Three", 0,
		            msg, null);
			ErrorDialog.openError(this.getShell(), "Invalid Choice", "Invalid Choice", status);
			
		}

	}
}

/**
 * This class returns the files in the specified directory. If the specified
 * directory doesn't exist, it returns an empty array.
 */

class BackupFilesContentProvider implements ITreeContentProvider {
	private static final Object[] EMPTY = new Object[] {};

	HashMap<Suite, Card[]> cards = new HashMap<Suite, Card[]>(4);

	public BackupFilesContentProvider() {
		for (Card[] c : Card.getFreshPackPerSuite()) {
			cards.put(c[0].getSuite(), c);
		}
	}

	/**
	 * Called when the input changes
	 */
	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {

	}

	@Override
	public Object[] getChildren(Object node) {

		if (node instanceof String)
			return Suite.values();

		if (node instanceof Suite) {
			return cards.get((Suite) node);
		}
		return EMPTY;
	}

	@Override
	public Object getParent(Object node) {
		if (node instanceof Suite) {
			return null;
		}
		return ((Card) node).getSuite();
	}

	@Override
	public boolean hasChildren(Object node) {
		if (node instanceof Suite || node instanceof String) {
			return true;
		}
		return false;
	}

	@Override
	public Object[] getElements(Object node) {
		if (node instanceof String)
			return Suite.values();

		if (node instanceof Suite) {
			return cards.get((Suite) node);
		}
		return EMPTY;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
}

/**
 * This class provides the labels for files
 */

class BackupFilesLabelProvider implements ILabelProvider {
	/**
	 * Returns the image
	 * 
	 * @param arg0
	 *            the file
	 * @return Image
	 * @Override
	 */
	public Image getImage(Object node) {
		if (node instanceof Suite) {
			Suite c = (Suite) node;
			switch (c) {
			case CLUB:
				return Activator.getImageDescriptor("icons/club.gif").createImage();
			case DIAMOND:
				return Activator.getImageDescriptor("icons/diamond.gif").createImage();
			case SPADE:
				return Activator.getImageDescriptor("icons/spade.gif").createImage();
			case HEART:
				return Activator.getImageDescriptor("icons/heart.gif").createImage();
			}
		}
		return null;
	}

	/**
	 * Returns the name of the file
	 * 
	 * @param arg0
	 *            the name of the file
	 * @return String
	 * @Override
	 */
	public String getText(Object node) {

		if (node instanceof String)
			return node.toString();

		if (node instanceof Suite)
			return "     ";
		else
			return ((Card) node).getFace().getFace();

	}

	@Override
	public void addListener(ILabelProviderListener arg0) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {

	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}