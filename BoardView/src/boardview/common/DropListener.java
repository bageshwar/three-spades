/**
 * 
 */
package boardview.common;

import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;

/**
 * @author Bageshwar
 *
 */
public class DropListener implements DropTargetListener {
	
	@Override
	public void dragEnter(DropTargetEvent arg0) {
		// TODO Auto-generated method stub

		System.out.println("Drop Enter");
	}

	@Override
	public void dragLeave(DropTargetEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Drop Leave");
	}

	@Override
	public void dragOperationChanged(DropTargetEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Drop Changed");
	}

	@Override
	public void dragOver(DropTargetEvent arg0) {
		// TODO Auto-generated method stub

		System.out.println("Drop Over");
	}

	@Override
	public void drop(DropTargetEvent arg0) {
		// TODO Auto-generated method stub

		System.out.println("Dropped");
	}

	@Override
	public void dropAccept(DropTargetEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Drop Accept");
	}

}
