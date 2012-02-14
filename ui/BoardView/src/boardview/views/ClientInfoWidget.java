/**
 * 
 */
package boardview.views;

import org.eclipse.swt.SWT;
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

	Text icon;
	Label name;
	/**
	 * @return the icon
	 */
	public Text getIcon() {
		return icon;
	}

	/**
	 * @return the name
	 */
	public Label getName() {
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
		this.setLayout(gd);
		
		icon = toolkit.createText(this, "Icon",SWT.NONE);
		
		name = toolkit.createLabel(this, "Name",SWT.NONE);
		
		ping = toolkit.createLabel(this, "Ping",SWT.NONE);
		
		icon.setSize(200,80);
		name.setSize(200,80);
		ping.setSize(200,80);
	}

}
