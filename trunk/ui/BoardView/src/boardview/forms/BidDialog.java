/**
 * 
 */
package boardview.forms;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.techno.blackthree.common.RoundParameters;

/**
 * @author Bageshwar
 *
 */
public class BidDialog extends Dialog {


	public BidDialog(Shell parentShell,RoundParameters roundParams) {
		super(parentShell);
		this.roundParams = roundParams;
	}
	public static final String ID = "boardview.forms.BidDialog";
	
	private String bid;
	private RoundParameters roundParams;
	
	private Text bidText;
	
	public String getBid(){
	return 	bid;
	}
	
	
	@Override
	 protected Control createDialogArea(Composite parent) {
		
	      Composite composite = (Composite)super.createDialogArea(parent);
	      
	      FormToolkit toolkit = new FormToolkit(parent.getDisplay());
	      Form form = toolkit.createForm(parent);	      
	      GridLayout layout = new GridLayout();
	      form.getBody().setLayout(layout);
	      if(roundParams!=null && roundParams.getKingPlayer()!=null){
	      Label label1 = toolkit.createLabel(form.getBody(), roundParams.getKingPlayer()+" placed the highest bid @"+roundParams.getMaxBid());
	      }else {
	    	  Label label1 = toolkit.createLabel(form.getBody(), "No highest Bidder Yet");
	      }
	      Label label2 = toolkit.createLabel(form.getBody(), "Bid:");
	       bidText = toolkit.createText(form.getBody(), "150");
	      
	      
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
	      newShell.setText("Bid");
	      
	   }

		@Override
		protected void okPressed() {
		System.out.println("OK");
		bid = bidText.getText();		
		super.okPressed();
		}	
}
