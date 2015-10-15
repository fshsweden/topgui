package com.fsh.topgui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.ev112.codeblack.atc.connections.PriceCollectorEventHandler;
import com.ev112.codeblack.atc.connections.RiskControllerConnectionEventHandler;
import com.ev112.codeblack.atc.connections.StrategyServerConnectionEventHandler;
import com.ev112.codeblack.simpleclient.alphasystem.AlphaSystem;
import com.ev112.codeblack.simpleclient.alphasystem.AlphaSystemConnectionEventHandler;
import com.ev112.codeblack.simpleclient.alphasystem.AlphaSystemModule;
import com.ev112.codeblack.simpleclient.alphasystem.AlphaSystemStatus;
import com.fsh.topgui.framework.BaseFrame;
import com.fsh.topgui.framework.Coordinate;
import com.fsh.topgui.framework.Workspace;
import com.fsh.topgui.models.OrderTradeFrame;
import com.fsh.topgui.models.OwnOrderTradesTableModel;
import com.fsh.topgui.models.ServerStatusFrame;
import com.fsh.topgui.models.ServerStatusTableModel;


public class ApplicationWindow {

	private Workspace wkspc;
	private JFrame frame;
	private static AlphaSystem alpha = new AlphaSystem("TESTREMOTE");
	
	public static AlphaSystem getAlphaInstance() {
		return alpha;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ApplicationWindow window = new ApplicationWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ApplicationWindow() {
		
		wkspc = new Workspace(alpha);
		
		initialize_gui();
		initialize_alpha();
	}
	
	private PriceCollectorEventHandler	pcEH;
	private StrategyServerConnectionEventHandler	ssEH;
	private RiskControllerConnectionEventHandler	rmEH;
	
	private void initialize_alpha() {
		alpha.connect(new AlphaSystemConnectionEventHandler() {
			@Override
			public void alphaConnectionStatus(AlphaSystemModule module, AlphaSystemStatus status) {
				switch (module) {
					case PriceCollector:
						if (status == AlphaSystemStatus.Connected)
						{
							System.out.println("PriceCollector disconnected....");
						}
						else
						{
							
						}
					break;
					case RiskManager:
						if (status == AlphaSystemStatus.Connected)
						{
							System.out.println("Riskmanager disconnected....");
						}
						else
						{
							
						}
					break;
					case StrategyServer:
						if (status == AlphaSystemStatus.Connected)
						{
							System.out.println("StrategyServer connected....");
						}
						else
						{
							System.out.println("StrategyServer disconnected....");
						}
					break;
				}
				
//				for (JFrame jf : wkspc.getAllFrames()) {
//					jf.alphaConnectionStatus(module, status);					
//				}
				
			}
		});
	}
	

	
	public void createNewEventsFrame() {
//		Coordinate c = wkspc.getCoordinateForNewWindow();
//		EventsTableModel model = new EventsTableModel(alphaSystem);
//		EventsFrame pd = new EventsFrame(model);
//		pd.setLocation(c.getX(),c.getY());
//		pd.setVisible(true);
//		wkspc.addWorkspaceItem(pd);
	}

	public void createNewPositionFrame() {
//		Coordinate c = wkspc.getCoordinateForNewWindow();
//		PositionTableModel ptm = new PositionTableModel(alphaSystem.getStrategyServerConnection(), "some-id", null /* owner window */);
//		PositionFrame pd = new PositionFrame(ptm);
//		pd.setLocation(c.getX(),c.getY());
//		pd.setVisible(true);
//		wkspc.addWorkspaceItem(pd);
	}


//	public void createNewTestWindow() {
//		Coordinate c = coordinator.getCoordinateForNewWindow();
//		TestWindow tw = new TestWindow(alphaSystem);
//		tw.setLocation(c.getX(),c.getY());
//		tw.addWindowListener(this);
//		tw.setVisible(true);
//		addWorkspaceItem(tw);
//	}

	public void createNewServerStatusFrame() {
		
		ServerStatusTableModel sstm = new ServerStatusTableModel();
		
		ServerStatusFrame tw = new ServerStatusFrame(sstm);	// A frame has one or more models! each table has one. 
		
		Coordinate c = wkspc.getCoordinateForNewWindow();
		tw.setLocation(c.getX(),c.getY());
		tw.setVisible(true);
		wkspc.addWorkspaceItem(tw);
	}
	
	public void createNewStrategyFrame() {
//		Coordinate c = wkspc.getCoordinateForNewWindow();
//		
//		// StrategyDetailTableModel stm = new StrategyDetailTableModel(alphaSystem.getStrategyServerConnection(), "ss-id", null/* final Window pOwner, PositionTableModel pPositionModel*/);
//		PositionTableModel ptm = new PositionTableModel(alphaSystem.getStrategyServerConnection(), "ptm-id", null/* final Window pOwner, PositionTableModel pPositionModel*/);
//		StrategyFrame s = new StrategyFrame(ptm);
//		s.setLocation(c.getX(),c.getY());
//		
//		s.setVisible(true);
//		
//		wkspc.addWorkspaceItem(s);
	}
	
	
	public BaseFrame createNewBaseFrame(String type) {
		BaseFrame bf;
		Coordinate c = wkspc.getCoordinateForNewWindow();
		switch (type) {
			case "OrderTrade":
				OwnOrderTradesTableModel ootm = new OwnOrderTradesTableModel("any-id");
				OrderTradeFrame tw = new OrderTradeFrame(ootm);
				tw.setLocation(c.getX(),c.getY());
				tw.setVisible(true);
				wkspc.addWorkspaceItem(tw);
				bf = tw;
			break;
			
			default:
				return null;
		}
		bf.setLocation(c.getX(),c.getY());
		bf.setVisible(true);
		return bf;
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize_gui() {
		frame = new JFrame();

	    JMenuBar menuBar = new JMenuBar();

	    //
	    //	Open a position window
	    //
	    JMenuItem orderTradeMenuItem = new JMenuItem("New OrderTrade Window");
	    orderTradeMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BaseFrame bf = createNewBaseFrame("OrderTrade");
				wkspc.addWorkspaceItem(bf);
			}
		});

	    //
	    //
	    //
	    JMenuItem saveWkspcMenuItem = new JMenuItem("Save Workspace");
	    saveWkspcMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				wkspc.saveWorkspace();
			}
		});

	    //
	    //
	    //
	    JMenuItem loadWkspcMenuItem = new JMenuItem("Load Workspace");
	    loadWkspcMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				wkspc.loadWorkspaceAndInstantiateWindows();
			}
		});
	    
	    // build the File menu
	    JMenu fileMenu = new JMenu("File");
	    fileMenu.add(orderTradeMenuItem);
	    fileMenu.add(saveWkspcMenuItem);
	    fileMenu.add(loadWkspcMenuItem);
	    
	    // add menus to menubar
	    menuBar.add(fileMenu);
	 
	    // put the menubar on the frame
	    frame.setJMenuBar(menuBar);
	 
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setBounds(0, 0, 689, 554);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	    frame.setPreferredSize(new Dimension(400, 300));
	    frame.pack();
//	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);		
	}
}
