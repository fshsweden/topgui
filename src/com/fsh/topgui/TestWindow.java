package com.fsh.topgui;
import javax.swing.JPanel;

import com.ev112.codeblack.simpleclient.alphasystem.AlphaSystem;

public class TestWindow extends EventsFrame {
	public TestWindow(AlphaSystem alpha) {
		super(alpha);
		setTitle("Test Window");
	}
	
	@Override
	public String getWindowType() {
		return "Test";
	}

	@Override
	protected JPanel createContentPane() {
		JPanel panel = super.createContentPane();
		
		
		
		
		return panel;
	}

}
