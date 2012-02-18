/**
 * 
 */
package boardview.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @author Bageshwar
 *
 */
public class ClientInfoWidget extends Composite {

	Label icon;
	Label name;
	/**
	 * @return the icon
	 */
	public Label getIcon() {
		return icon;
	}

	/**
	 * @return the name
	 */
	public Label getNameLabel() {
		return name;
	}

	/**
	 * @return the ping
	 */
	public Label getPing() {
		return ping;
	}

	Label ping;
	
	public ClientInfoWidget(Composite parent,FormToolkit toolkit ) {
		super(parent, SWT.NO_BACKGROUND);
		
		//create an image [con/discon]
		//label player name
		// ping 
		
		GridLayout gd  = new GridLayout();
		gd.numColumns = 3;
		gd.horizontalSpacing=20;
		//gd.
		this.setLayout(gd);
		
		icon = toolkit.createLabel(this, "Icon",SWT.NONE);		
		GridData g1 = new GridData(20,20);
		g1.grabExcessHorizontalSpace=true;
		g1.horizontalAlignment = SWT.LEFT;
		icon.setLayoutData(g1);
		
		name = toolkit.createLabel(this, "Name",SWT.NONE);
		GridData g2 = new GridData(200,20);
		g2.grabExcessHorizontalSpace=true;
		g2.horizontalAlignment = SWT.LEFT;
		name.setLayoutData(g2);
		
		ping = toolkit.createLabel(this, "Ping",SWT.NONE);
		GridData g3 = new GridData(40,20);
		g3.grabExcessHorizontalSpace=true;
		g3.horizontalAlignment = SWT.LEFT;
		ping.setLayoutData(g3);
		
/*		icon.setSize(200,80);
		name.setSize(200,80);
		ping.setSize(200,80);
*/	}

}
