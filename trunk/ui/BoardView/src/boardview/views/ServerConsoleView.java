/**
 * 
 */
package boardview.views;

import java.io.IOException;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;
import org.techno.blackthree.common.Codes;
import org.techno.blackthree.common.event.GameEvent;
import org.techno.blackthree.common.event.GameEventListener;
import org.techno.blackthree.server.Server;

import boardview.Activator;

/**
 * @author Bageshwar
 * 
 */
public class ServerConsoleView extends ViewPart implements GameEventListener {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "boardview.views.ServerConsoleView";

	private Server server;

	private Display display;

	private Text logger;
	private FormToolkit toolkit;
	private Action start;
	private Composite monitor;

	private SashForm form;

	@Override
	public void createPartControl(Composite parent) {
		
		/*Server s = Activator.getDefault().getServer();
		if(s!=null){
			for(GameEventListener gel:s.getController().getGameEventListeners()){
				if(gel instanceof ServerConsoleView){
					//this = gel;
					ServerConsoleView sc = (ServerConsoleView) gel;
					this.form = sc.form;
					this.monitor = sc.monitor;
					this.logger = sc.logger;
					this.server = sc.server;
					this.start = sc.start;
					this.toolkit = sc.toolkit;
					
					return;
				}
			}
		}*/
		
		display = parent.getDisplay();

		toolkit = new FormToolkit(parent.getDisplay());
		form = new SashForm(parent, SWT.HORIZONTAL);

		form.setLayout(new FillLayout());
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.verticalSpacing = 30;

		monitor = toolkit.createComposite(form,SWT.NONE);
		monitor.setLayout(layout);

		logger = toolkit.createText(form, "", SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		logger.setEditable(false);

		designMonitorPanel();

		initActions();

		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(start);

	}

	private void fillLocalPullDown(IMenuManager menuManager) {
		// TODO Auto-generated method stub

	}

	private void designMonitorPanel() {

		for (int i = 0; i < Server.PLAYER_COUNT; i++) {

			ClientInfoWidget w = new ClientInfoWidget(monitor, toolkit);

		}

	}

	private void initActions() {
		start = new Action() {
			public void run() {
				display.asyncExec(new Runnable() {

					@Override
					public void run() {
						if(server==null){
							//logger.append("initing Server\r\n");
							server = new Server(new String[] { "8" });
							server.addGameEventListener(ServerConsoleView.this);
						}
						if(server.isStarted()){
							System.out.println("stopping server\r\n");
							//stop server change icon
							server.stop();
							start.setText("Start");
							start.setToolTipText("Start Server");
							start.setImageDescriptor(Activator.getImageDescriptor("icons/green.png"));
							logger.setText(""); //resetting text
							
							//reset the panel's text
							for(int i=0;i<monitor.getChildren().length;i++){
							((ClientInfoWidget) (monitor.getChildren()[i])).getIcon().setText("");
							((ClientInfoWidget) (monitor.getChildren()[i])).getIcon().setImage(
									Activator.getImageDescriptor("icons/disconnected.jpg").createImage());
							((ClientInfoWidget) (monitor.getChildren()[i])).getNameLabel().setText("");
							((ClientInfoWidget) (monitor.getChildren()[i])).getPing().setText( "");
							}
						}
						else {
							//start server change icon
							new Thread(new Runnable() {
								@Override
								public void run() {
									//
									try {
										server.start();
										start.setImageDescriptor(Activator.getImageDescriptor("icons/red.png"));
									} catch (IOException e) {
										try{
										showMessage(e.getMessage());
										}catch(Exception ee){
											e.printStackTrace();
											//ee.printStackTrace();
										}
									}	
								}}).start();
							
							start.setText("Stop");
							start.setToolTipText("Stop Server");
							start.setImageDescriptor(Activator.getImageDescriptor("icons/blue.png"));
						}
						Activator.getDefault().setServer(server);						
					}

				});
			}
		};

		start.setText("Start");
		start.setToolTipText("Start Server");
		start.setImageDescriptor(Activator.getImageDescriptor("icons/green.png"));
	}

	@Override
	public void setFocus() {
		// form.setFocus();

		monitor.setFocus();
	}

	@Override
	public void consumeGameEvent(final GameEvent gameEvent) {
		// TODO Auto-generated method stub

		display.asyncExec(new Runnable() {

			@Override
			public void run() {

				String code = gameEvent.getCode();

				if (code.equals(Codes.PLAYER_UPDATE)) {
					updatePlayer(gameEvent);
				}
				else if(code.equals(Codes.LOG_MESSAGE)){
					logger.append(gameEvent.getPayLoad().toString()+"\r\n");
				}

			}
		});

	}

	@SuppressWarnings("unchecked")
	protected void updatePlayer(GameEvent gameEvent) {

		List payload = (List) gameEvent.getPayLoad();

		Integer index = (Integer) payload.get(0);
		String status = (String) payload.get(1);
		String playerName = (String) payload.get(2);
		String hostName = (String) payload.get(3);

		((ClientInfoWidget) (monitor.getChildren()[index])).getIcon().setText(status);
		((ClientInfoWidget) (monitor.getChildren()[index])).getIcon().setImage(
				Activator.getImageDescriptor("icons/connected.jpg").createImage());
		((ClientInfoWidget) (monitor.getChildren()[index])).getNameLabel().setText(playerName + "@" + hostName);
		((ClientInfoWidget) (monitor.getChildren()[index])).getPing().setText(System.currentTimeMillis() + "");

	}

	private void startServer() {
		new Thread(new Runnable() {
			public void run() {
				Server server = new Server(new String[] { "8" });
				try {
					server.start();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(display.getActiveShell(), "Board View", message);
	}
	

	@Override
	public void dispose() {
		if (server.isStarted()) {
			showMessage("The Game Server will be stopped now");
			// stop teh server
			server.stop();
		}
		super.dispose();
	}	
}
