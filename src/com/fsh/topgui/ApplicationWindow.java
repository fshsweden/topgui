package com.fsh.topgui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class ApplicationWindow implements WindowListener {

	private Workspace wkspc = new Workspace();

	/*
	 * Places new windows where there is free space
	 */
	public class Coordinator {
		public Coordinate getCoordinateForNewWindow() {
			return new Coordinate(100,100);
		}
	}
	private Coordinator coordinator = new Coordinator();
	
	private JFrame frame;

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
		initialize();
	}

	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		

	    JMenuBar menuBar = new JMenuBar();
	    

	    //
	    //	Open a position window
	    //
	    JMenuItem posMenuItem = new JMenuItem("New Position Window");
	    posMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Coordinate c = coordinator.getCoordinateForNewWindow();
				PositionWindow pd = new PositionWindow();
				pd.addWindowListener(ApplicationWindow.this);
				pd.setVisible(true);
				wkspc.addWorkspaceItem(pd);
			}
		});

	    //
	    //
	    //
	    JMenuItem orderMenuItem = new JMenuItem("New Order Window");
	    orderMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Coordinate c = coordinator.getCoordinateForNewWindow();
				OrderWindow ow = new OrderWindow();
				ow.addWindowListener(ApplicationWindow.this);
				ow.setVisible(true);
				wkspc.addWorkspaceItem(ow);
			}
		});
	    
	    //
	    //
	    //
	    JMenuItem tradeMenuItem = new JMenuItem("New Trade Window");
	    tradeMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Coordinate c = coordinator.getCoordinateForNewWindow();
				TradeWindow tw = new TradeWindow();
				
				tw.addWindowListener(ApplicationWindow.this);
				
				tw.setVisible(true);
				wkspc.addWorkspaceItem(tw);
			}
		});
	    
	    // build the File menu
	    JMenu fileMenu = new JMenu("File");
	    fileMenu.add(posMenuItem);
	    fileMenu.add(orderMenuItem);
	    fileMenu.add(tradeMenuItem);

	    JMenuItem saveWkspcMenuItem = new JMenuItem("Save Workspace");
	    saveWkspcMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				wkspc.saveWorkspace();
			}
		});
	    fileMenu.add(saveWkspcMenuItem);

	    JMenuItem loadWkspcMenuItem = new JMenuItem("Load Workspace");
	    loadWkspcMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				wkspc.clear();
				
				List<WindowInfo> w = wkspc.loadWorkspace();
				for (WindowInfo wi : w) {
					switch (wi.get("WindowType")) {
						case "Trade":
						{
							Coordinate c = coordinator.getCoordinateForNewWindow();
							TradeWindow pd = new TradeWindow();
							pd.addWindowListener(ApplicationWindow.this);
							pd.setVisible(true);
							
							pd.setLocation(wi.getInt("X"), wi.getInt("Y"));
							pd.setSize(wi.getInt("Width"), wi.getInt("Height"));
							
							wkspc.addWorkspaceItem(pd);
						}
						break;
						case "Order":
						{
							Coordinate c = coordinator.getCoordinateForNewWindow();
							OrderWindow ow = new OrderWindow();
							ow.addWindowListener(ApplicationWindow.this);
							ow.setVisible(true);

							ow.setLocation(wi.getInt("X"), wi.getInt("Y"));
							ow.setSize(wi.getInt("Width"), wi.getInt("Height"));
							
							wkspc.addWorkspaceItem(ow);
						}
						break;
						case "Position":
						{
							Coordinate c = coordinator.getCoordinateForNewWindow();
							PositionWindow pd = new PositionWindow();
							pd.addWindowListener(ApplicationWindow.this);
							pd.setVisible(true);

							pd.setLocation(wi.getInt("X"), wi.getInt("Y"));
							pd.setSize(wi.getInt("Width"), wi.getInt("Height"));
							
							wkspc.addWorkspaceItem(pd);
						}
						break;
					}
				}
			}
		});
	    fileMenu.add(loadWkspcMenuItem);
	    
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

	@Override
	public void windowOpened(WindowEvent e) {
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println("Closing! " + e.toString());
		if (e.getWindow() instanceof BaseWindow) {
			BaseWindow bw = (BaseWindow)e.getWindow();
			wkspc.removeWorkspaceItem(bw);
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		Point loc = e.getWindow().getLocation();
		Dimension dim = e.getWindow().getSize();
		
		JFrame frame = (JFrame)e.getWindow();
		WorkspaceItem wi = wkspc.getWorkspaceItem(frame);
		if (frame != null) {
			
		}
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		
	}

}
