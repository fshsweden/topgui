package com.fsh.topgui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.ev112.codeblack.simpleclient.alphasystem.AlphaSystem;
import com.ev112.codeblack.simpleclient.alphasystem.AlphaSystemModule;
import com.ev112.codeblack.simpleclient.alphasystem.AlphaSystemStatus;
import com.ev112.codeblack.simpleclient.alphasystem.IAlphaSystemConnectionStatus;
import com.fsh.topgui.framework.Workspace;


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
	
	private void initialize_alpha() {
		alpha.connect(new IAlphaSystemConnectionStatus() {
			@Override
			public void alphaConnectionStatus(AlphaSystemModule module, AlphaSystemStatus status) {
				switch (module) {
					case PriceCollector:
					break;
					case RiskManager:
					break;
					case StrategyServer:
					break;
				}
				
//				for (JFrame jf : wkspc.getAllFrames()) {
//					jf.alphaConnectionStatus(module, status);					
//				}
				
			}
		});
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
	    JMenuItem posMenuItem = new JMenuItem("New Position Window");
	    posMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				wkspc.createNewPositionFrame();
			}
		});

	    //
	    //
	    //
	    JMenuItem orderMenuItem = new JMenuItem("New Order Window");
	    orderMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				wkspc.createNewOrderFrame();
			}
		});
	    
	    //
	    //
	    //
	    JMenuItem tradeMenuItem = new JMenuItem("New Trade Window");
	    tradeMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				wkspc.createNewTradeFrame();
			}
		});

	    //
	    //
	    //
	    JMenuItem testMenuItem = new JMenuItem("New Test Window");
	    testMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				wkspc.createNewTestWindow();
			}
		});

	    //
	    //
	    //
	    JMenuItem eventsMenuItem = new JMenuItem("New Events Window");
	    eventsMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				wkspc.createNewEventsFrame();
			}
		});
	    
	    //
	    //
	    //
	    JMenuItem statusMenuItem = new JMenuItem("New Server Status Window");
	    statusMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				wkspc.createNewServerStatusFrame();
			}
		});
	    
	    //
	    //
	    //
	    JMenuItem strategyMenuItem = new JMenuItem("Strategy Window");
	    strategyMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				wkspc.createNewStrategyFrame();
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
	    fileMenu.add(posMenuItem);
	    fileMenu.add(orderMenuItem);
	    fileMenu.add(tradeMenuItem);
	    fileMenu.add(testMenuItem);
	    fileMenu.add(statusMenuItem);
	    fileMenu.add(eventsMenuItem);
	    fileMenu.add(saveWkspcMenuItem);
	    fileMenu.add(loadWkspcMenuItem);
	    fileMenu.add(strategyMenuItem);
	    
	    // build the Edit menu
	    JMenu editMenu = new JMenu("Edit");
	    JMenuItem cutMenuItem = new JMenuItem("Cut");
	    JMenuItem copyMenuItem = new JMenuItem("Copy");
	    JMenuItem pasteMenuItem = new JMenuItem("Paste");
	    editMenu.add(cutMenuItem);
	    editMenu.add(copyMenuItem);
	    editMenu.add(pasteMenuItem);
	 
	    // add menus to menubar
	    menuBar.add(fileMenu);
	    menuBar.add(editMenu);
	 
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
