package com.fsh.topgui.models;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import com.ev112.codeblack.atc.ParameterEntry;
import com.ev112.codeblack.atc.lookandfeel.AtcTableRenderer;
import com.ev112.codeblack.workbench.gui.tools.StaticColumnData;

/** Copyright EV112 Development AB, Stockholm, Sweden 2010 */

/**
 * Model for strategy statistics
 */
public class StatisticsTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	// Define columns
	private final StaticColumnData[] mColumnData = {
		  new StaticColumnData("Name", 120, JLabel.LEFT, String.class, null, null)
		, new StaticColumnData("Value", 30, JLabel.LEFT, String.class, null, null)
	};
	
	// GUI components: Table and Renderer
	final JTable mTable;
	final AtcTableRenderer mResultRenderer;

	// The list of parameters
	ArrayList<ParameterEntry> mParameters = new ArrayList<ParameterEntry>();

	/**
	 * Constructor
	 */
	StatisticsTableModel() {
		mResultRenderer = new AtcTableRenderer(mColumnData);
		for (StaticColumnData tData : mColumnData) {
			tData.mRenderer = mResultRenderer;
		}

		mTable = new JTable(this);
		mTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mTable.setGridColor(Color.BLACK);

		// Set table attributes
		mResultRenderer.setColumnAttributes(mTable.getColumnModel(), mColumnData);
		mTable.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
		mTable.getTableHeader().setDefaultRenderer(mResultRenderer);			
	}
	
	/**
	 * Get a component for the table
	 */
	Component getComponent() {
		return mTable;
	}
	
	/**
	 * Add a parameter to model, always at end
	 * @param pName
	 * @param pValue
	 */
	void addParameterEntry(String pName, String pValue) {
		int l = 0;
		for (ParameterEntry tEntry : mParameters) {
			if (tEntry.getName().equals(pName)) {
				tEntry.setValue(pValue);
				fireTableCellUpdated(l, 1);
				return;
			}
			++l;
		}
		mParameters.add(new ParameterEntry(pName, pValue));
		l = mParameters.size();
		fireTableRowsInserted(l-1, l-1);
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		return mParameters.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ParameterEntry tEntry = mParameters.get(rowIndex);
		return columnIndex == 0 ? tEntry.getName() : tEntry.getValue();
	}
}

