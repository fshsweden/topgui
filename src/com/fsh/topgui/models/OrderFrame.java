package com.fsh.topgui.models;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.fsh.topgui.framework.BaseFrame;

import net.miginfocom.swing.MigLayout;

public class OrderFrame extends BaseFrame {

	public OrderFrame() {
		super();
		setTitle("Order Window");
	}

	@Override
	public String getWindowType() {
		return "Order";
	}

	@Override
	protected JPanel createContentPane() {
		
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(new MigLayout("fillx,filly", "[100][600,grow][100]", "[][][][][][][][][][][][][][]"));
		
		/*
		 * Create a FLOWLAYOUT ButtonPane, with an OK and CANCEL
		 */
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		contentPanel.add(buttonPane, "cell 0 13,alignx left,aligny top");
		JButton okButton = new JButton("Place Order");
		buttonPane.add(okButton);
		okButton.setActionCommand("OK");
		
		getRootPane().setDefaultButton(okButton);
		
		{
			
			JButton cancelButton = new JButton("Skip Order");
			buttonPane.add(cancelButton);
			cancelButton.setActionCommand("Cancel");
		}
		
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
			
		return contentPanel;
		
	}
	
	

}
