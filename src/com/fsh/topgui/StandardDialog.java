package com.fsh.topgui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class StandardDialog extends JDialog implements WorkspaceItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 956335172179297963L;
	
	private final JPanel contentPanel = new JPanel();
	private Integer x,y,height,width;

	/**
	 * Create the dialog.
	 */
	public StandardDialog(Integer x, Integer y, Integer width, Integer height) {
		
		if (x == null) {
			x = new Integer(100);
		}
		if (y == null) {
			y = new Integer(100);
		}
		if (width == null) {
			width = new Integer(300);
		}
		if (height == null) {
			height = new Integer(300);
		}
				
		
		
		this.height = height;
		this.width = width;
		
		setBounds(x,y,473,248);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	@Override
	public String getWindowType() {
		return "Standard";
	}

	@Override
	public Integer getWindowPositionX() {
		return getLocation().x;
	}

	@Override
	public Integer getWindowPositionY() {
		return getLocation().y;
	}

	@Override
	public Integer getWindowWidth() {
		return getSize().width;
	}

	@Override
	public Integer getWindowHeight() {
		return getSize().height;
	}

	
	public static void main(String[] args) {
		new StandardDialog(100, 100, 300, 300).setVisible(true);
	}
}
