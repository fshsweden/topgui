package com.fsh.topgui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class ApplicationWindow {

	private Workspace wkspc = new Workspace();
	
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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

	    JMenuBar menuBar = new JMenuBar();
	    
	    // build the File menu
	    JMenu fileMenu = new JMenu("File");
	    JMenuItem openMenuItem = new JMenuItem("Open");
	    openMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Coordinate c = coordinator.getCoordinateForNewWindow();
				WorkspaceDlg pd = new WorkspaceDlg(100, 100, 450,320);
				pd.setVisible(true);
				
				wkspc.addWorkspaceItem(pd);
			}
		});
	    fileMenu.add(openMenuItem);

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
				wkspc.loadWorkspace();
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
	    frame.setPreferredSize(new Dimension(400, 300));
	    frame.pack();
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);		
	}

}
