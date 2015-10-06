package com.fsh.topgui.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;

import com.ev112.codeblack.common.model.parameters.Parameter;
import com.ev112.codeblack.workbench.gui.tools.StaticColumnData;

/** Copyright EV112 Development AB, Stockholm, Sweden 2010 */

public class ParameterTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	// Engine
// 	private TradingStrategyServiceInterface mServiceInterface;    // REMOVED!! 
	
	// Columns
	private final StaticColumnData[] mColumnData = {
		  new StaticColumnData("Name", 275, JLabel.LEFT, String.class, null, null)
		, new StaticColumnData("Value", 100, JLabel.LEFT, String.class, null, null)
		, new StaticColumnData("Name", 275, JLabel.LEFT, String.class, null, null)
		, new StaticColumnData("Value", 100, JLabel.LEFT, String.class, null, null)
	};
	
	public StaticColumnData[] getColumnData() { return mColumnData; }
	
	List<Parameter> mParameters = new ArrayList<Parameter>();
	
	
	public ParameterTableModel(/*TradingStrategyServiceInterface pServiceInterface*/) {
		
		// mServiceInterface = pServiceInterface;
		
//		Strategy tTrigger = mServiceInterface.getStrategy(AbstractTriggerStrategy.class);
//		if (tTrigger != null) {
//			mParameters.addAll(tTrigger.getParameters().getParameterList());
//		}
//		
//		Strategy tEvent = mServiceInterface.getStrategy(AbstractEventStrategy.class);
//		if (tEvent != null) {
//			mParameters.addAll(tEvent.getParameters().getParameterList());
//		}
		
		Collections.sort(mParameters);
	}
	
	public void clear() {
		mParameters.clear();
	}

	@Override
	public int getColumnCount() {
		return mColumnData.length;
	}

	@Override
	public int getRowCount() {
		return (mParameters.size() / 3) + 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		int nRows = (mParameters.size() / 3) + 1;
		int c = columnIndex / 2;
		int i = c * nRows + rowIndex;
		
		if (i < mParameters.size()) {
			Parameter p = mParameters.get(i);
			
			if (columnIndex % 2 == 0) {
				return p.getIdentity();
			} else {
				return p.getValue().toString();
			}
		}
		return null;
	}
	
}
