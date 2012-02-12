/**
 * 
 */
package boardview.forms;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @author Bageshwar
 *
 */
public class ConnectDialog extends Dialog {


	public ConnectDialog(Shell parentShell) {
		super(parentShell);
		
	}
	public static final String ID = "boardview.forms.ConnectDialog";
	
	private String host;
	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}
	private String port;
	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}
	private String name;
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	public void setDefaultValues(String n,String h,String p){
		host=h;
		port=p;
		name=n;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	private Text hostText;
	private Text portText;
	private Text nameText;
	
	public String getHost(){
	return 	host;
	}
	
	
	public String getPort(){
		return port;
	}
	
	@Override
	 protected Control createDialogArea(Composite parent) {
		
	      Composite composite = (Composite)super.createDialogArea(parent);
	      
	      FormToolkit toolkit = new FormToolkit(parent.getDisplay());
	      Form form = toolkit.createForm(parent);	      
	      GridLayout layout = new GridLayout();
	      form.getBody().setLayout(layout);
	      
	      Label label3 = toolkit.createLabel(form.getBody(), "Your Name:");
	       nameText = toolkit.createText(form.getBody(), name);
	       
	      Label label1 = toolkit.createLabel(form.getBody(), "Host:");
	       hostText  = toolkit.createText(form.getBody(), host);
	      Label label2 = toolkit.createLabel(form.getBody(), "Port:");
	       portText = toolkit.createText(form.getBody(), port);
	      
	      
	      /*Button button = toolkit.createButton(parent, "A checkbox in a form", SWT.CHECK);
	      GridData gd = new GridData();
	      gd.horizontalSpan = 2;
	      button.setLayoutData(gd);*/
		/*Canvas canvas = new Canvas(parent,SWT.WRAP);
		GridLayout layout = new GridLayout();
	      canvas.setLayout(layout);
		hostText = new Text(canvas,SWT.WRAP);
		hostText.setText("127.0.0.1");
		portText = new Text(canvas,SWT.WRAP);
		portText.setText("12346");
		Label l1 = new Label(canvas,SWT.WRAP);
		l1.setText("Host");*/
	      return composite;
	      
	   }
	@Override
	protected void configureShell(Shell newShell) {
	      super.configureShell(newShell);
	      newShell.setText("Connect to a Server");
	      
	   }

		@Override
		protected void okPressed() {
		System.out.println("OK");
		host = hostText.getText();
		port = portText.getText();
		name = nameText.getText();
		super.okPressed();
		}	
}
