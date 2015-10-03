package com.fsh.topgui;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.ev112.codeblack.simpleclient.alphasystem.AlphaSystem;
import com.ev112.codeblack.simpleclient.alphasystem.AlphaSystemModule;
import com.ev112.codeblack.simpleclient.alphasystem.AlphaSystemStatus;
import com.ev112.codeblack.simpleclient.alphasystem.IAlphaSystemConnectionStatus;

public abstract class BaseFrame extends JFrame  implements WorkspaceItem, IAlphaSystemConnectionStatus {

	private AlphaSystem alphaSystem;
	private String windowType;
	
	/**
	 * Create the frame.
	 */
	public BaseFrame(AlphaSystem alphaSystem) {
		
		this.alphaSystem = alphaSystem;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 347, 481);
		// setPreferredSize(preferredSize);
		setContentPane(createContentPane());
		
		addComponentListener(new ComponentListener() {
		    @Override
			public void componentResized(ComponentEvent e) {
		        System.out.println("BaseFrame::componentResized");           
		    }

			@Override
			public void componentMoved(ComponentEvent e) {
		        System.out.println("BaseFrame::componentMoved");           
			}

			@Override
			public void componentShown(ComponentEvent e) {
		        System.out.println("BaseFrame::componentShown");           
			}

			@Override
			public void componentHidden(ComponentEvent e) {
		        System.out.println("BaseFrame::componentHidden");           
			}
		});		
		
		
		pack();
		setVisible(true);
	}
	
	// Derived classes implement this!
	protected abstract JPanel createContentPane();
	
	@Override
	public String getWindowType() {
		return windowType;
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

	@Override
	public JFrame getFrame() {
		return this;
	}

	@Override
	public void setWindowPosition(Integer x, Integer y) {
		setLocation(x, getLocation().y);
	}

	@Override
	public void setWindowSize(Integer w, Integer h) {
		setSize(w, h);
	}

	@Override
	public void setWindowType(String t) {
		// TODO implement this
		// error, shouldn't be possible to overwrite type??
	}

	@Override
	public void alphaConnectionStatus(AlphaSystemModule module, AlphaSystemStatus status) {
		
	}

}
