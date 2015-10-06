package com.fsh.topgui.models;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import com.ev112.codeblack.atc.StrategyDetailedModel;
import com.ev112.codeblack.atc.lookandfeel.AtcTableRenderer;
import com.ev112.codeblack.common.generated.messages.NameValuePair;
import com.ev112.codeblack.common.strategy.StrategyStatus.STRATEGY_SOURCE;
import com.ev112.codeblack.common.strategy.StrategyStatus.STRATEGY_STATUS;
import com.ev112.codeblack.workbench.gui.tools.StaticColumnData;



/** Copyright EV112 Development AB, Stockholm, Sweden 2010 */

/**
 * Models the state history
 */
public class StateHistoryTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	// Define columns
	private final StaticColumnData[] mColumnData = {
		  new StaticColumnData("Entry", 50, JLabel.LEFT, String.class, null, null)
		, new StaticColumnData("State", 50, JLabel.LEFT, String.class, null, null)
		, new StaticColumnData("Substate", 100, JLabel.LEFT, String.class, null, null)
		, new StaticColumnData("Last Reason", 200, JLabel.LEFT, String.class, null, null)
	};
	
	// GUI components: Table and Renderer
	final JTable mTable;
	final AtcTableRenderer mResultRenderer;
	
	// List of state history entries
	ArrayList<StateHistoryEntry> mStates = new ArrayList<StateHistoryEntry>();
	
	// Table ID
	final String mId;
	
	
	/**
	 * Constructor
	 */
	StateHistoryTableModel(String pId) {
		mId = pId;
		mResultRenderer = new AtcTableRenderer(mColumnData);

		mTable = new JTable(this);
		mTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mTable.setGridColor(Color.BLACK);
		mTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// Set table attributes
		StaticColumnData.setColumnAttributes(mColumnData, mTable, mId, mResultRenderer, null);
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
	 * Update/add a state
	 */
	void updateState(List<NameValuePair> pAllParameters) {
		String tEntryTime = null, tState = null, tSubState = null, tLastReason = null;
		
		for (NameValuePair tPair : pAllParameters) {
			if (tPair.getSource().equals(STRATEGY_SOURCE.STATE.toString())) {
				if (tPair.getName().equals(STRATEGY_STATUS.STATE_ENTRY.toString())) {
					tEntryTime = StrategyDetailedModel.getParameterValue(tPair);
				} else if (tPair.getName().equals(STRATEGY_STATUS.STATE.toString())) {
					tState = StrategyDetailedModel.getParameterValue(tPair);
				} else if (tPair.getName().equals(STRATEGY_STATUS.SUB_STATE.toString())) {
					tSubState = StrategyDetailedModel.getParameterValue(tPair);
				} else if (tPair.getName().equals(STRATEGY_STATUS.LAST_REASON.toString())) {
					tLastReason = StrategyDetailedModel.getParameterValue(tPair);
				}
			}
		}
		
		StateHistoryEntry tEntry = getLastEntry();
		if (tEntry != null) {
			if (compareString(tEntry.mTime, tEntryTime)
				&& compareString(tEntry.mState, tState)
				&& compareString(tEntry.mSubState, tSubState)
				&& compareString(tEntry.mLastReason, tLastReason))
			{
				// All equal, no need to add
				return;
			}
		}
		
		// Either first entry, or different
		mStates.add(new StateHistoryEntry(tEntryTime, tState, tSubState, tLastReason));
		fireTableRowsInserted(0, 0);
	}
	
	public StateHistoryEntry getLastEntry() {
		return mStates.isEmpty() ? null : mStates.get(mStates.size() - 1);
	}

	@Override
	public int getColumnCount() {
		return mColumnData.length;
	}

	@Override
	public int getRowCount() {
		return mStates.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		StateHistoryEntry tEntry = mStates.get(mStates.size() - 1 - rowIndex);
		switch (columnIndex) {
			case 0: return tEntry.mTime;
			
			case 1: return tEntry.mState;
			
			case 2: return tEntry.mSubState;
			
			case 3: return tEntry.mLastReason;
		}
		return null;
	}
	
	boolean compareString(String s1, String s2) {
		if (s1 == null && s2 == null) {
			return true;
		}
		return s1 == null || s2 == null ? false : s1.equals(s2);
	}
	
	void savePreferences(Preferences pPrefs) {
		StaticColumnData.savePreferences(pPrefs, mColumnData, mTable, mId);
	}
	
}
