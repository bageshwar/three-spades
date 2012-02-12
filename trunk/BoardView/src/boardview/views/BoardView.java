package boardview.views;


import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.part.ViewPart;
import org.techno.blackthree.client.Client;
import org.techno.blackthree.common.Card;
import org.techno.blackthree.common.Codes;
import org.techno.blackthree.common.Face;
import org.techno.blackthree.common.Move;
import org.techno.blackthree.common.Suite;
import org.techno.blackthree.common.event.GameEvent;
import org.techno.blackthree.common.event.GameEventListener;
import org.techno.blackthree.server.Server;

import boardview.Activator;
import boardview.common.BoardDesigner;
import boardview.forms.BidDialog;
import boardview.forms.ConnectDialog;
import boardview.forms.DealDialog;
import boardview.forms.PartnerDialog;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class BoardView extends ViewPart implements GameEventListener  {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "boardview.views.BoardView";
	Client client;
	//private TableViewer viewer;
	private Action action1;
	private Action action2;
	//private Action doubleClickAction;
	private Action roundDetails;

	private Display display = null;
	
	private Form form;
	private Composite deal;
	private Composite hand;
	
	Card[] cards = new Card[] { new Card(Suite.CLUB, Face.ACE) };
	
	private Action connect;
	private Action score;
	private Action host;
	
	
	/**
	 * The constructor.
	 */
	public BoardView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		
		display = parent.getDisplay();
		/*viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider(cards));
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());*/
		
		
		Object[] c =BoardDesigner.createBoardSkeleton(parent, form, deal, hand,8,6);
		form = (Form) c[0];
		deal = (Composite) c[1];
		hand = (Composite) c[2];
		BoardDesigner.arrangeDeal(deal, new ArrayList<Move>(), 8,"40");
		BoardDesigner.arrangeHand(hand, new ArrayList<Card>(), 6,"40");
		
		
		// Create the help context id for the viewer's control
		//PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "BoardView.viewer");
		makeActions();
		/*hookContextMenu();
		hookDoubleClickAction();*/
		contributeToActionBars();
	}

	/*private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				BoardView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}*/

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(connect);
		manager.add(score);
		manager.add(roundDetails);
		manager.add(new Separator());
		manager.add(host);
	}

	/*private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}*/
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(connect);
		manager.add(score);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		roundDetails = new Action(){
			public void run(){
				if(client!=null && client.getRoundParams()!=null)
					showMessage(client.getRoundParams().toString());
			}
		};
		roundDetails.setText("Round Details");
		roundDetails.setToolTipText("Round Details");
		roundDetails.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_DEF_VIEW));
		
		connect = new Action() {
			public void run() {
				
				//get the ip address and port number
				ConnectDialog connectDialog = new ConnectDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
				connectDialog.setDefaultValues(Activator.getDefault().getPlayerName(), 
						Activator.getDefault().getDefaultHost(),
						Activator.getDefault().getDefaultPort()
				
				);
				connectDialog.open();
				
				if(connectDialog.getReturnCode()==Dialog.OK)
				try {
					client = new Client(connectDialog.getHost(),Integer.parseInt(connectDialog.getPort())
							,connectDialog.getName());
					client.addEventListener(BoardView.this);
					new Thread(client).start();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						showMessage(e.getMessage());
					}
					Activator.getDefault().setClient(client);
					Activator.getDefault().setPlayerName(connectDialog.getName());
					Activator.getDefault().setDefaultPort(connectDialog.getPort());
					Activator.getDefault().setDefaultHost(connectDialog.getHost());
					
				} catch (UnknownHostException e) {
					e.printStackTrace();
					showMessage(e.getMessage());
				} catch (IOException e) {
					e.printStackTrace();
					showMessage(e.getMessage());
				}
			}
		};
		connect.setText("Connect");
		connect.setToolTipText("Connect to a Server");
		connect.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_ETOOL_HOME_NAV));
		
		host = new Action() {
			public void run() {
				
			
			new Thread(new Runnable() {
				public void run() {
					Server server = new Server(new String[]{"8"});
					server.start();
					Activator.getDefault().setServer(server);	
				}
			}).start();
		}
		};
		host.setText("Host");
		host.setToolTipText("Host a new Game");
		host.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD));

		score = new Action() {
			public void run() {
				if(client!=null)
				showMessage("User Score: "+client.getPlayer().getScore());
				
				updateBoard(null);
				
				/*ArrayList<Move> a = new ArrayList<Move>();
				a.add(new Move(new Card(Suite.DIAMOND,Face.KING)));
				a.add(new Move(new Card(Suite.CLUB,Face.QUEEN)));
				a.add(new Move(new Card(Suite.SPADE,Face.JACK)));
				a.add(new Move(new Card(Suite.HEART,Face.TEN)));
				
				ArrayList<Card> myCards = new ArrayList<Card>();
				myCards.add(new Card(Suite.DIAMOND,Face.KING));
				myCards.add(new Card(Suite.CLUB,Face.QUEEN));
				myCards.add(new Card(Suite.SPADE,Face.JACK));
				myCards.add(new Card(Suite.HEART,Face.TEN));
				myCards.add(new Card(Suite.CLUB,Face.TEN));
				myCards.add(new Card(Suite.SPADE,Face.ACE));
				
				DealDialog dd =  new DealDialog(display.getActiveShell(),a);
				dd.setMyCards(myCards);
				dd.open();
*/				
			}
		};
		score.setText("Score");
		score.setToolTipText("Your Score");
		score.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_LCL_LINKTO_HELP));

		
		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		/*doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showMessage("Double-click detected on "+obj.toString());
			}
		};*/
	}

	/*private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}*/
	private void showMessage(String message) {
		MessageDialog.openInformation(
			display.getActiveShell(),
			"Board View",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		//viewer.getControl().setFocus();
		form.setFocus();
	}
	

	@Override
	public void consumeGameEvent(final GameEvent gameEvent) {
		/**
		 * Handle various kinds of events here.
		 * */
		
		
		
		display.syncExec(new Runnable(){
			public void run(){
				String code = gameEvent.getCode();		
			
		
		
		if (code.equals(Codes.GIVE_PLAYER)){
		return;
		}
		else if (code.equals(Codes.PLAYER_UPDATE)){
			updatePlayersDetails(gameEvent);
		}
		else if (code.equals(Codes.BID)){
			placeBid(gameEvent);
		}
		else if (code.equals(Codes.BID_UPDATE)){ // TODO: remove bid update and use
			// round parameters update
			//updateBidDetails();
		}
		else if (code.equals(Codes.GAME_OVER)){
			//gameOver();
		}
		else if (code.equals(Codes.ADHOC_MESSAGE)){
			//processAdhocMessage();
		}
		else if (code.equals(Codes.ACCEPT_HAND)){
			acceptDeal(gameEvent);
		}
		else if (code.equals(Codes.KINGS_SPEECH)){
			sendPartnerCardsAndTriumph(gameEvent);
		}
		else if (code.equals(Codes.ROUND_PARAMETERS_UPDATE)){
			//updateRoundDetails();
		}
		else if (code.equals(Codes.MOVE)){
			makeAMove(gameEvent);
		}
		else if(code.equals(Codes.BOARD_UPDATE)){
			updateBoard(gameEvent);
		}
		else if(code.equals(Codes.SCORE_UPDATE)){
			//need to reset the deal
			BoardDesigner.arrangeDeal(deal, new ArrayList<Move>(), 8, "40");
		}
			}

			
		});
	}

	private void updateBoard(GameEvent gameEvent) {
		try {
			BoardDesigner.arrangeDeal(deal, (ArrayList<Move>) gameEvent.getPayLoad(), 8, "40");
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("boardview.views.BoardView");
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		//PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViews()[0]);
		//PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findViewReference("ProjectExplorer");
		
	}

	
	private void makeAMove(GameEvent gameEvent) {
		DealDialog dd  = new DealDialog(display.getActiveShell(),gameEvent.getPayLoad());
		dd.setMyCards(client.getPlayer().getCards());
		dd.setRoundParams(client.getRoundParams());
		dd.open();
		
		
		if(dd.getReturnCode()==Dialog.OK){
			gameEvent.setResponse(dd.getMyCard());
			//cards.
		}
	}
	
	private void sendPartnerCardsAndTriumph(GameEvent gameEvent) {
		
		PartnerDialog pd = new PartnerDialog(display.getActiveShell());
		pd.open();
		if(pd.getReturnCode()==Dialog.OK){
			gameEvent.setResponse(new Object[]{pd.getTriumph(),pd.getPartnerCards()});
		}
		
	}
	
	private void placeBid(GameEvent gameEvent) {
		BidDialog bidDialog = new BidDialog(display.getActiveShell(),client.getRoundParams());
		bidDialog.open();
		if(bidDialog.getReturnCode()==Dialog.OK)
			gameEvent.setResponse(Integer.parseInt(bidDialog.getBid()));
		
	}
	
	private void updatePlayersDetails(GameEvent gameEvent) {
		//showMessage(gameEvent.getPayLoad().toString()+" has joined");
		
		System.out.println(gameEvent.getPayLoad().toString()+" has joined");
	}

	private void acceptDeal(GameEvent gameEvent) {
		/*viewer.remove(cards);
		cards=((ArrayList<Card>)(gameEvent.getPayLoad())).toArray(cards);		
		((ViewContentProvider)viewer.getContentProvider()).setCards(cards);		
		viewer.add(cards);			
		viewer.refresh();
		return;*/
		BoardDesigner.arrangeHand(hand, client.getPlayer().getCards(), 6, "40");		
	}

}