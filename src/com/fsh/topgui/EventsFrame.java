package com.fsh.topgui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.ev112.codeblack.simpleclient.alphasystem.AlphaSystem;

public class EventsFrame extends BaseFrame {
	public EventsFrame(AlphaSystem alpha) {
		super(alpha);
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
		
		
		JPanel middlePanel = new JPanel();
		middlePanel.setBackground(Color.GREEN);
		
//			middlePanel.setLayout(new MigLayout());
//			middlePanel.add(new JLabel("Enter size:"),   "");
//			middlePanel.add(new JTextField("",4),          "wrap");
//			middlePanel.add(new JLabel("Enter weight:"), "");
//			middlePanel.add(new JTextField("",8),          "");	
		
			
			JTable table = new JTable(new EventsModel());
			JScrollPane scrollPane = new JScrollPane(table);
			
			middlePanel.add(scrollPane);
		
		
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
