package com.fsh.topgui.models;

import javax.swing.JPanel;

import com.fsh.topgui.framework.BaseFrame;

public class PositionFrame extends BaseFrame {

	PositionTableModel positionModel;
	
	public PositionFrame(PositionTableModel ptm) {
		super();
		positionModel = ptm;
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
