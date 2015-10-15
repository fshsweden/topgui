package com.fsh.topgui.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.ev112.codeblack.atc.connections.PriceCollectorEventHandler;
import com.ev112.codeblack.atc.connections.RiskControllerConnectionEventHandler;
import com.ev112.codeblack.atc.connections.StrategyServerConnectionEventHandler;
import com.ev112.codeblack.common.generated.messages.PLStrategy;
import com.ev112.codeblack.common.generated.messages.PriceCollectorClockPulseBdx;
import com.ev112.codeblack.common.generated.messages.PriceCollectorFacilityBdx;
import com.ev112.codeblack.common.generated.messages.PriceCollectorQuoteBdx;
import com.ev112.codeblack.common.generated.messages.PriceCollectorReplayStartedBdx;
import com.ev112.codeblack.common.generated.messages.PriceCollectorReplayStoppedBdx;
import com.ev112.codeblack.common.generated.messages.PriceCollectorTradeBdx;
import com.ev112.codeblack.common.generated.messages.RiskController_PositionStatusBdx;
import com.ev112.codeblack.common.generated.messages.StatusEvent;
import com.ev112.codeblack.common.generated.messages.StrategyServer_OwnOrder;
import com.ev112.codeblack.common.generated.messages.StrategyServer_OwnTrade;
import com.ev112.codeblack.simpleclient.alphasystem.AlphaSystem;
import com.ev112.codeblack.simpleclient.alphasystem.IAlphaSystemEventHandler;
import com.ev112.codeblack.workbench.gui.tools.StaticColumnData;
import com.fsh.topgui.framework.BaseTableModel;

/*	The EventsTableModel handles 
 * 		- the event data for a JTable  (extends AbstractTableModel)
 * 		- 
 * 
 * 
 * 
 */
public class EventsTableModel extends BaseTableModel implements IAlphaSystemEventHandler, StrategyServerConnectionEventHandler, RiskControllerConnectionEventHandler, PriceCollectorEventHandler {

	private static final long serialVersionUID = -8132531768705472386L;

	// Define columns
	private final StaticColumnData[] mColumnData = {
		  new StaticColumnData("Server", 60, JLabel.LEFT, String.class, null, null)
		, new StaticColumnData("Time", 50, JLabel.LEFT, String.class, null, null)
		, new StaticColumnData("Severity", 50, JLabel.LEFT, String.class, null, null)
		, new StaticColumnData("Message", 400, JLabel.LEFT, String.class, null, null)
	};

	// Time formatter
	private final SimpleDateFormat mTimeFormatter = new SimpleDateFormat("HH:mm:ss.SSS");
	
	// Events
	private final ArrayList<StatusEvent> mEvents = new ArrayList<StatusEvent>();
	private final ArrayList<RowData> mRows = new ArrayList<RowData>();
	
	// Checkboxes
	private final JCheckBox mReverseTimeBox = new JCheckBox("Reverse time", true);
	private final JCheckBox mInfoBox = new JCheckBox("Information", true);
	private final JCheckBox mErrorBox = new JCheckBox("Error", true);
	private final JCheckBox mPriceBox = new JCheckBox("Price Collector", true);
	private final JCheckBox mStrategyBox = new JCheckBox("Strategy Server", true);
	private final JCheckBox mRiskBox = new JCheckBox("Risk Controller", true);
	
	
	// Last event -- to make sure we don't swamp the user
//	private long mLastEventTime = 0L;
//	private boolean mSwamped = false;
	
	/**
	 * ServerTableModel constructor
	 * <p>
	 * Build ServerData structures and start connecting
	 */
	public EventsTableModel(AlphaSystem a) {
		super(a);
		super.setupColumns(mColumnData);
		
		alphaSystem.addSystemEventHandler(this);
		alphaSystem.addStrategyHandler(this);
		alphaSystem.addRiskControllerEventHandler(this);
		alphaSystem.addPriceCollectorEventHandler(this);
	}
	
	
	/**
	 * Set column attributes
	 */
	public void setColumnAttributes(TableColumnModel pColumnModel) {
		for (int i = 0; i < mColumnData.length; i++) {
			TableColumn tColumn = pColumnModel.getColumn(i);
			if (mColumnData[i].mHeader != null) {
				tColumn.setHeaderValue(mColumnData[i].mHeader);
			}
			if (mColumnData[i].mWidth > 0) {
				tColumn.setPreferredWidth(mColumnData[i].mWidth);
				tColumn.setMinWidth(mColumnData[i].mWidth);
			}
			tColumn.setCellRenderer(mRenderer);
			tColumn.setHeaderRenderer(mRenderer);
			if (mColumnData[i].mEditor != null) {
				tColumn.setCellEditor(mColumnData[i].mEditor);
			}
		}
	}
	
	@Override
	public int getColumnCount() {
		return mColumnData.length;
	}

	@Override
	public int getRowCount() {
		return mRows.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		StatusEvent tEvent = getRowDataForRow(rowIndex).getEvent();
		switch (columnIndex) {
			case 0:
				return tEvent.getServer(); 
				
			case 1:
				return mTimeFormatter.format(tEvent.getTimestamp());
				
			case 2:
				return tEvent.getSeverityString();
				
			case 3:
				return tEvent.getMessage();
				
			default:
				return null;
		}
	}
	
	public RowData getRowDataForRow(int rowIndex) {
		int tRow = mReverseTimeBox.isSelected() ? mRows.size() - 1 - rowIndex : rowIndex;
		return mRows.get(tRow);
	}
	
	public void addEvents(List<StatusEvent> pEvents) {
		
//		long tCurrentTime = System.currentTimeMillis();
//		if (pEvents.size() > 1 || tCurrentTime < mLastEventTime + 5 * 60 * 1000) {
//			if (mSwamped == false) {
//				// Just setting swamped -- show messagebox once
//				mSwamped = true;
//				try {
//					new MessageBox(ATCMain.getInstance(), "ERROR", ATCMain.getInstance().getGuiEnvironment().getTableBackgroundColor() )
//						.showText("Multiple status events at the same time, please check event window", true);
//				} catch( Exception e ) {
//					e.printStackTrace();
//				}			
//			}
//		} else {
//			mSwamped = false;
//			mLastEventTime = tCurrentTime;
//		}
//		
		boolean tNewDisplayedEvent = false;
		for (StatusEvent tEvent : pEvents) {
			mEvents.add(tEvent);
			if (includeEvent(tEvent)) {
				addRow(tEvent);
				tNewDisplayedEvent = true;
			}
		
//			if (tEvent.getSeverity() == ATC_StatusEvent.Error && !mSwamped) {
//				try {
//					new MessageBox(ATCMain.getInstance(), "ERROR", ATCMain.getInstance().getGuiEnvironment().getTableBackgroundColor() )
//						.showText(tEvent.getMessage(), true);
//				} catch( Exception e ) {
//					e.printStackTrace();
//				}
//			}
		}
		
		if (tNewDisplayedEvent) {
			fireTableDataChanged();
		}
	}
	
	private boolean includeEvent(StatusEvent pEvent) {
		
		// Information?
		if (mInfoBox.isSelected() == false && pEvent.getSeverity() == StatusEvent.Information) {
			return false;
		}
		
		// Error?
		if (mErrorBox.isSelected() == false && pEvent.getSeverity() == StatusEvent.Error) {
			return false;
		}
		
		// Price server
		if (mPriceBox.isSelected() == false && pEvent.getServer().equals("PriceCollector")) {
			return false;
		}
		
		// Strategy server
		if (mStrategyBox.isSelected() == false && pEvent.getServer().equals("StrategyServer")) {
			return false;
		}
		
		// Risk server
		if (mRiskBox.isSelected() == false && pEvent.getServer().equals("RiskController")) {
			return false;
		}
		
		return true;
	}
	
	private void addRow(StatusEvent pEvent) {
		for (int i = 0; i < mRows.size(); i++) {
			RowData tRowData = mRows.get(i); 
			if (tRowData.mEvent.getTimestamp() > pEvent.getTimestamp()) {
				mRows.add(i, new RowData(pEvent));
				return;
			}
		}
		
		// Add at end
		mRows.add(new RowData(pEvent));
	}
	
	/**
	 * Helper class to wrap ATC_StatusEvent
	 */
	class RowData {
		StatusEvent mEvent;
		
		RowData(StatusEvent pEvent) {
			mEvent = pEvent;
		}
		
		StatusEvent getEvent() {
			return mEvent;
		}
		
		String getTooltipText() {
			return mEvent.getMessage();
		}
	}
	
	public void reloadEvents() {
		mRows.clear();
		for (StatusEvent tEvent : mEvents) {
			if (includeEvent(tEvent)) {
				addRow(tEvent);
			}
		}
		fireTableDataChanged();
	}


	
	
	@Override
	public void handleEvent(StatusEvent pEvents) {
		// TODO implement this
		
	}


	@Override
	public void addEvent(List<StatusEvent> pEvents) {
		// TODO implement this
		
	}


	@Override
	public void strategyLoaded(String strategy, Integer status) {
		// TODO implement this
		
	}


	@Override
	public void strategyUnloaded(String strategy, Integer status) {
		// TODO implement this
		
	}


	@Override
	public void strategyListReply(List<PLStrategy> pStrategies) {
		// TODO implement this
		
	}


	@Override
	public void strategyListUpdated(List<PLStrategy> pStrategies) {
		// TODO implement this
		
	}


	@Override
	public void setMatcher(String pMatcher) {
		// TODO implement this
		
	}


	@Override
	public void addOrder(StrategyServer_OwnOrder pOrder, boolean pBroadcast) {
		// TODO implement this
		
	}


	@Override
	public void addTrade(StrategyServer_OwnTrade pTrade, boolean pBroadcast) {
		// TODO implement this
		
	}


	@Override
	public void priceCollectorTradeEvent(PriceCollectorTradeBdx pBdx) {
		// TODO implement this
		
	}


	@Override
	public void priceCollectorQuoteEvent(PriceCollectorQuoteBdx pBdx) {
		// TODO implement this
		
	}


	@Override
	public void priceCollectorReplayStarted(PriceCollectorReplayStartedBdx pBdx) {
		// TODO implement this
		
	}


	@Override
	public void priceCollectorReplayStopped(PriceCollectorReplayStoppedBdx pBdx) {
		// TODO implement this
		
	}


	@Override
	public void priceCollectorClockPulseEvent(PriceCollectorClockPulseBdx pBdx) {
		// TODO implement this
		
	}


	@Override
	public void facilityUpdate(PriceCollectorFacilityBdx pBdx) {
		// TODO implement this
		
	}


	@Override
	public void priceCollectorStatusEvent(List<StatusEvent> pEvents) {
		// TODO implement this
		
	}


	@Override
	public void updateRiskData(RiskController_PositionStatusBdx pRiskUpdate) {
		// TODO implement this
		
	}
	

	
	
	/*
	
	
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
	*/
}
