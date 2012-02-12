/**
 * 
 */
package boardview.common;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.techno.blackthree.common.Card;

import boardview.Activator;

/**
 * @author Bageshwar
 *
 */
public class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
	public String getColumnText(Object obj, int index) {
		//return getText(obj);
		return ((Card)obj).getFace().getFace();
	}
	public Image getColumnImage(Object obj, int index) {
		return getImage(obj);
	}
	public Image getImage(Object obj) {
		
		Card c = (Card) obj;
		switch(c.getSuite()){
		case CLUB: return Activator.getImageDescriptor("icons/club.gif").createImage();
		case DIAMOND: return Activator.getImageDescriptor("icons/diamond.gif").createImage();
		case SPADE: return Activator.getImageDescriptor("icons/spade.gif").createImage();
		case HEART: return Activator.getImageDescriptor("icons/heart.gif").createImage();
		}
		return null;
		
	}
}
