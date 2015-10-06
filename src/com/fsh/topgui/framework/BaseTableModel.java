package com.fsh.topgui.framework;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import com.ev112.codeblack.simpleclient.alphasystem.AlphaSystem;
import com.ev112.codeblack.workbench.gui.tools.StaticColumnData;

public abstract class BaseTableModel extends AbstractTableModel {

	protected AlphaSystem alphaSystem;	
	protected DefaultTableCellRenderer mRenderer = null;
	
	public BaseTableModel(AlphaSystem a) {
		alphaSystem = a;
	}
	
	protected void setupColumns(StaticColumnData[] columnData) {
		mRenderer = new AlphaTableRenderer(columnData);
		for (StaticColumnData tData : columnData) {
			tData.mRenderer = mRenderer;
		}
	}

}
