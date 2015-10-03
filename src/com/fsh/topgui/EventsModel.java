package com.fsh.topgui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public class EventsModel extends AbstractTableModel {

	private static final long serialVersionUID = -8132531768705472386L;

	public class Row {
		Map<Integer,String> colValues = new HashMap<Integer,String>();
		public Row() {
			
		}
		public void addColValue(Integer col, String val) {
			colValues.put(col, val);
		}
		public String getColValue(Integer col) {
			return colValues.get(col);
		}
	}
	Map<Integer,Row> rows = new HashMap<Integer,Row>();
	
	public EventsModel() {
		// Connect to data here!
		
		Row r = new Row();
		r.addColValue(0, "column 0");
		r.addColValue(1, "column 1");
		r.addColValue(2, "column 2");
		
		rows.put(0, r);
		
		r.addColValue(0, "column 0");
		r.addColValue(1, "column 1");
		r.addColValue(2, "column 2");
		
		rows.put(1, r);
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				
				int rowcount=2;
				while (true) {
					
					Row r = new Row();
					
					r.addColValue(0, "column 0");
					r.addColValue(1, "column 1");
					r.addColValue(2, "column 2");
					
					rows.put(rowcount, r);
					
					fireTableDataChanged();
					
					rowcount++;
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		
		new Thread(runnable).start();
	}
	
	
	@Override
	public int getRowCount() {
		return rows.size();
	}

	@Override
	public int getColumnCount() {
		// TODO implement this
		return 3;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Row r = rows.get(rowIndex);
		if (r == null) {
			return null;
		}
		return r.getColValue(columnIndex);
	}

}
