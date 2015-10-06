package com.fsh.topgui.models;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.fsh.topgui.framework.BaseFrame;

public class ServerStatusFrame extends BaseFrame {
	
	public ServerStatusFrame() {
		super();
	}

	@Override
	public String getWindowType() {
		return "Type2";
	}

	
	@Override
	protected JPanel createContentPane() {
		
		JPanel panel = new JPanel(new BorderLayout());
		
		JPanel topPanel = new JPanel();
		topPanel.setBackground(Color.BLUE);
		
			topPanel.setLayout(new FlowLayout());
			
				JTextField search = new JTextField();
				search.setColumns(10);
				
			topPanel.add(search);
			topPanel.add(new JButton("START"));
			topPanel.add(new JButton("START"));
		
		/*
		 * 
		 */
			
		JPanel middlePanel = new JPanel();
			middlePanel.setLayout(new BorderLayout());
			middlePanel.setBackground(Color.GREEN);
		
			JTable table = new JTable(new ServerStatusTableModel());
			JScrollPane scrollPane = new JScrollPane(table);
			
			middlePanel.add(scrollPane, BorderLayout.CENTER);
		
			
		/*
		 * 
		 */
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.BLACK);

			bottomPanel.setLayout(new FlowLayout());
			bottomPanel.add(new JButton("START"));
			bottomPanel.add(new JButton("START"));
			bottomPanel.add(new JButton("START"));
		
		
		
		panel.add(topPanel, BorderLayout.PAGE_START);
		panel.add(middlePanel, BorderLayout.CENTER);
		panel.add(bottomPanel, BorderLayout.PAGE_END);
		
		return panel;
	}

}
