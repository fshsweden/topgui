package com.fsh.topgui;

import javax.swing.JPanel;

import com.ev112.codeblack.simpleclient.alphasystem.AlphaSystem;

public class PositionWindow extends BaseFrame {

	public PositionWindow(AlphaSystem alpha) {
		super(alpha);
		setTitle("Position Window");
	}

	@Override
	public String getWindowType() {
		return "Position";
	}

	@Override
	protected JPanel createContentPane() {
		
		// return empty panel
		return new JPanel();
	}

}
