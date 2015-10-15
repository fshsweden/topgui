package com.fsh.topgui.models;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.table.AbstractTableModel;

import com.ev112.codeblack.atc.ATCMain;
import com.ev112.codeblack.atc.PlotIFrame;
import com.ev112.codeblack.atc.lookandfeel.AtcFormatters;
import com.ev112.codeblack.common.generated.messages.StrategyServer_OwnOrder;
import com.ev112.codeblack.common.generated.messages.StrategyServer_OwnTrade;
import com.ev112.codeblack.common.ordercontroller.OrderStates;
import com.ev112.codeblack.common.ordercontroller.OrderTradeAction;
import com.ev112.codeblack.common.ordercontroller.OrderTypeX;
import com.ev112.codeblack.common.strategy.OrderTradeTableEntry.TYPE;
import com.ev112.codeblack.common.strategy.PlotContent;
import com.ev112.codeblack.workbench.gui.tools.MessageBox;
import com.ev112.codeblack.workbench.gui.tools.StaticColumnData;
import com.fsh.topgui.framework.AlphaTableRenderer;

/** Copyright EV112 Development AB, Stockholm, Sweden 2010 */

/**
 * Manager, and owner, of orders and trades in the application
 */
public class OwnOrderTradesTableModel extends AbstractTableModel{
	
	private static final long serialVersionUID = 1L;

	// Define columns
	public final StaticColumnData[] mColumnData = {
		  new StaticColumnData("Time"		, 60, JLabel.LEFT, String.class, null, null)
		, new StaticColumnData("Type"		, 50, JLabel.LEFT, String.class, null, null)
		, new StaticColumnData("OrdTyp"		, 20, JLabel.LEFT, String.class, null, null)
		, new StaticColumnData("Symbol"		, 50, JLabel.LEFT, String.class, null, null)
		, new StaticColumnData("VolxPrice"	, 80, JLabel.RIGHT, String.class, null, null)
		, new StaticColumnData("Strategy"	, 80, JLabel.LEFT, String.class, null, null)
//		  new StaticColumnData("Time", 60, JLabel.LEFT, String.class, null, null)
//		, new StaticColumnData("Strategy", 80, JLabel.LEFT, String.class, null, null)
//		, new StaticColumnData("Type", 15, JLabel.LEFT, String.class, null, null)
//		, new StaticColumnData("OrdType", 15, JLabel.LEFT, String.class, null, null)
//		, new StaticColumnData("Symbol", 30, JLabel.LEFT, String.class, null, null)
//		, new StaticColumnData("B/S", 10, JLabel.LEFT, String.class, null, null)
//		, new StaticColumnData("Vol", 20, JLabel.RIGHT, String.class, null, null)
//		, new StaticColumnData("Price", 20, JLabel.RIGHT, String.class, null, null)
//		, new StaticColumnData("State", 30, JLabel.LEFT, String.class, null, null)
//		, new StaticColumnData("Order#", 40, JLabel.CENTER, String.class, null, null)
//		, new StaticColumnData("Reason", 20, JLabel.LEFT, String.class, null, null)
//		, new StaticColumnData("Own Ref", 50, JLabel.LEFT, String.class, null, null)
//		, new StaticColumnData("Condition", 15, JLabel.LEFT, String.class, null, null)
	};
	
	// Formatters
	private NumberFormat mPriceFormatter = NumberFormat.getInstance();
	private NumberFormat mVolumeFormatter = NumberFormat.getInstance();
	
	// Renderer
	final AlphaTableRenderer mResultRenderer;
	
	// List of own orders and trades;
	public final LinkedList<RowData> mRows = new LinkedList<RowData>();
	public final LinkedList<RowData> mSelectedRows = new LinkedList<RowData>();
	
	// Map of symbols - PlotIFrame
	private final HashMap<String, PlotIFrame> mPlotMap = new HashMap<String, PlotIFrame>();
	
	// Id
	public final String mId;
	
	/**
	 * ServerTableModel constructor
	 * <p>
	 * Build ServerData structures and start connecting
	 */
	public OwnOrderTradesTableModel(String pId) {
		mId = pId;
		mResultRenderer = new AlphaTableRenderer(mColumnData);
		for (StaticColumnData tData : mColumnData) {
			tData.mRenderer = mResultRenderer;
		}
		
		mPriceFormatter.setGroupingUsed(false);
		mPriceFormatter.setMaximumFractionDigits(2);
		mPriceFormatter.setMinimumFractionDigits(2);
		
		mVolumeFormatter.setGroupingUsed(false);
		mVolumeFormatter.setMaximumFractionDigits(4);
		mVolumeFormatter.setMinimumFractionDigits(0);

	}
	
	@Override
	public int getColumnCount() {
		return mColumnData.length;
	}

	@Override
	public int getRowCount() {
		return mSelectedRows.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		RowData tData = getRowDataForRow(rowIndex);
		switch (columnIndex) {
		
			case 0:	// Time
				return AtcFormatters.getInstance().getTimeWithMsFormatter().format(tData.getTime());

			case 1:	// Type
				return tData.getType();
				
			case 2: // Order type
				return tData.getOrderType();
				
			case 3: // Symbol
				return tData.getSymbol();
				
			case 4: // Vol x Price
				return (tData.getAction().equals(OrderTradeAction.Buy.toString()) ? "+" : "-") + mVolumeFormatter.format(tData.getVolume()) + " x " 
						+ mPriceFormatter.format(tData.getPrice());
				
			case 5: // Strategy
				return tData.getStrategyId();
				
			default:
				return null;
			
//			case 0:
//				return tTimeFormatter.format(tData.getTime()); 
//				 
//			case 1:
//				return tData.getStrategyId();
//				
//			case 2:
//				return tData.getType() == TYPE.OWN_ORDER ? "Order" : "Trade";
//				
//			case 3:
//				return tData.getOrderType();
//				
//			case 4:
//				return tData.getSymbol();
//				
//			case 5:
//				return tData.getAction().toString();
//				
//			case 6:
//				return tData.getVolumeAsString();
//				
//			case 7:
//				return mPriceFormatter.format(tData.getPrice());
//				
//			case 8:
//				return tData.getStateAsString();
//				
//			case 9:
//				return tData.getId();
//				
//			case 10:
//				return tData.getReason();
//				
//			case 11:
//				return tData.getOwnReference();
//				
//			case 12:
//				return tData.getTimeCondition();
//				
//			default:
//				return null;
		}
	}
	
	public RowData getRowDataForRow(int rowIndex) {
		return mSelectedRows.get(/* mReverseTimeBox.isSelected() ? mSelectedRows.size() - 1 - rowIndex : */ rowIndex);
	}
	
	public void addOrder(StrategyServer_OwnOrder pOrder, boolean pBroadcast) {
		
		// Check if order id has already been displayed
		for (RowData tRow : mRows) {
			if (tRow.getId().equals(pOrder.getOrderId())) {
				if (!tRow.getOwnReference().equals(pOrder.getOwnReference())) {
					System.out.println("Order id but not order reference matches!!!, id = " + pOrder.getOrderId());
				}
				if (tRow instanceof OrderRowData) {
					((OrderRowData) tRow).replaceOrder(pOrder);
					updateSelectedRows();
	//				sendRowToPlot(tRow);
					return;
				}
			}
		}
		
		// Come here if order doesn't exist before
		OrderRowData tRow = new OrderRowData(pOrder);
		insertRow(tRow);
		updateSelectedRows();
//		sendRowToPlot(tRow);
		
		// Show dialog if order is in error
		if (pOrder.getErrorStatus() != null && pOrder.getErrorStatus().length() > 0) {
			String s[] = new String[1];
			s[0] = pOrder.getErrorStatus();
			MessageBox.showText(
				ATCMain.getInstance(), 
				"ORDER ERROR", 
				"Order " + tRow.getOrderDataAsString(), 
				s, 
				ATCMain.getInstance().getGuiEnvironment().getTableBackgroundColor(), 
				ATCMain.getInstance().isProduction());
		}

//		if (mSoundBox.isSelected() && pBroadcast) {
//			beep();
//		}

	}
	
	public void addTrade(StrategyServer_OwnTrade pTrade, boolean pBroadcast) {
		RowData tRow = new TradeRowData(pTrade);
		insertRow(tRow);
//		sendRowToPlot(tRow);
		
		updateSelectedRows();

//		if (mSoundBox.isSelected() && pBroadcast) {
//			//beep();
//		}

	}
	
	void insertRow(RowData pRow) {
		int tIndex = 0;
		for (RowData tRow : mRows) {
			if (tRow.getTime() > pRow.getTime()) {
				mRows.add(tIndex, pRow);
				return;
			}
			++tIndex;
		}
		mRows.addLast(pRow);
	}
	
	void setSoundOn(boolean pOn) {
		/* mSoundBox.setSelected(pOn); */
	}
	
	/* boolean isSoundOn() { return mSoundBox.isSelected(); } */
	
	void beep() {
		/*
		if (mSoundBox.isSelected()) {
			try {
				PlayAudio.playFile("connect.wav");
			} catch( Exception e ) {
				e.printStackTrace();
			}
		}
		*/
	}
	
	void updateSelectedRows() {
		mSelectedRows.clear();
		for (RowData tRow : mRows) {
			
			// Screen entries with regard to active, order and trade
//			if (mActiveBox.isSelected() && !tRow.isActive()) {
//				continue;
//			}
			
			if (tRow.getType() == TYPE.OWN_ORDER /* && !mOrderBox.isSelected()*/ ) {
				continue;
			}
			
			if (tRow.getType() == TYPE.OWN_TRADE /* && !mTradeBox.isSelected() */ ) {
				continue;
			}
			
			mSelectedRows.add(tRow);
		}
		
		fireTableDataChanged();
	}
	
	/**
	 * Request orders and trades for a symbol
	 * Used by plot module.
	 * <p>
	 * First push down all current orders and trades, then all future ones
	 */
	public void requestOrdersAndTrades(String pSymbol, PlotIFrame pPlot) {
		// Remember that pPlot should receive orders and trades for the implicit symbol
		mPlotMap.put(pSymbol, pPlot);
		
		// Push down everything we have so far
//		for (RowData tRow : mRows) {
//			sendRowToPlot(tRow);
//		}
	}
	
	/**
	 * This method sends the contents of one row to the plotter
	 */
/*	
	private void sendRowToPlot(RowData pRow) {
		PlotIFrame tPlot = mPlotMap.get(pRow.getSymbol());
		if (tPlot != null) {
			if (pRow instanceof OrderRowData) {
				tPlot.addOrder(
					pRow.getTime(), 
					pRow.getAction().equals(OrderTradeAction.Buy.toString()), 
					pRow.getPrice(), 
					pRow);
			} else if (pRow instanceof TradeRowData) {
				tPlot.addTrade(
					pRow.getTime(), 
					pRow.getAction().equals(OrderTradeAction.Buy.toString()), 
					pRow.getPrice(), 
					pRow);
			}
		}
	}
*/
	
	/**
	 * Save preferences
	 */
//	public void savePreferences(Preferences pPrefs) {
//		StaticColumnData.savePreferences(pPrefs, mColumnData, mTable, mId);
//	}
	
	abstract class RowData implements PlotContent {
		public abstract String getId();
		public abstract TYPE getType();
		public abstract String getOrderType();
		public abstract long getTime();
		public abstract String getStrategyId();
		public abstract String getSymbol();
		public abstract String getAction();
		public abstract boolean isActive();
		public abstract int getVolume();
		public abstract double getPrice();
		public abstract String getReason();
		public abstract String getOwnReference();
		public abstract String getTimeCondition();
		public abstract String getTooltipText();
		public abstract String getVolumeAsString();
		public abstract String getStateAsString();
	}
	
	class OrderRowData extends RowData {
		
		StrategyServer_OwnOrder mOrder;
		
		OrderRowData(StrategyServer_OwnOrder pOrder) {
			mOrder = pOrder;
		}
		
		void replaceOrder(StrategyServer_OwnOrder pOrder) {
			mOrder = pOrder;
		}
		
		@Override
		public boolean isActive() {
			return OrderStates.isInMarket(mOrder.getSubState());
		}

		@Override
		public String getAction() {
			return mOrder.getAction();
		}

		@Override
		public String getId() {
			return mOrder.getOrderId();
		}

		@Override
		public String getOwnReference() {
			return mOrder.getOwnReference();
		}

		@Override
		public double getPrice() {
			return mOrder.getPrice();
		}

		@Override
		public String getReason() {
			return mOrder.getReason();
		}

		@Override
		public String getStrategyId() {
			return mOrder.getStrategyId();
		}

		@Override
		public String getSymbol() {
			return mOrder.getSymbol();
		}

		@Override
		public long getTime() {
			return mOrder.getCreateTime();
		}

		@Override
		public String getTimeCondition() {
			return mOrder.getTimeCondition();
		}

		@Override
		public TYPE getType() {
			return TYPE.OWN_ORDER;
		}

		@Override
		public int getVolume() {
			return mOrder.getCurrentVolume();
		}
		
		@Override
		public String getVolumeAsString() {
			return mVolumeFormatter.format(mOrder.getCurrentVolume()) 
				+ "/" + mVolumeFormatter.format(mOrder.getInitialVolume());
		}
		
		@Override
		public String getStateAsString() {
			return mOrder.getSubState(); 
		}
		
		@Override
		public String getTooltipText() {
			return
				getOrderDataAsString()
				+ (mOrder.getErrorStatus() != null ? " Error: " + mOrder.getErrorStatus() : "");
		}
		
		public String getOrderDataAsString() {
			
			String tPriceStr;
			if (getOrderType() != null && getOrderType().equals(OrderTypeX.MKT)) {
				tPriceStr = "MKT";
			} else {
				tPriceStr = mPriceFormatter.format(getPrice()); 
			}
			
			return 
				"ORDER "
				+ getSymbol() + " "
				+ (getAction().equals(OrderTradeAction.Buy.toString()) ? "+" : "-") + mVolumeFormatter.format(getVolume()) + " x " 
				+ tPriceStr 
				+ " " + getStrategyId()
				+ " " + getStateAsString()
				;
		}

		@Override
		public String toString(SimpleDateFormat pTimeFormatter,
				NumberFormat pPriceFormatter, NumberFormat pVolumeFormatter) {
			return getTooltipText();
		}

		@Override
		public String getOrderType() {
			return mOrder.getOrderType();
		}

	}
	
	/**
	 * Helper class to wrap a popup menu. The additional info is
	 * the current order being worked on.
	 */
	class OrderPopupMenu extends JPopupMenu {
		private static final long serialVersionUID = 1L;

		OrderRowData mOrderData;
		
		void setOrderRowData(OrderRowData pOrderData) {
			mOrderData = pOrderData;
		}
		
		OrderRowData getOrderRowData() { return mOrderData; }
	}
	
	/**
	 * Trade representation
	 */
	class TradeRowData extends RowData {
		
		final StrategyServer_OwnTrade mTrade;
		
		TradeRowData(StrategyServer_OwnTrade pTrade) {
			mTrade = pTrade;
		}

		@Override
		public String getAction() {
			return mTrade.getAction();
		}

		@Override
		public String getId() {
			return mTrade.getOrderId();
		}

		@Override
		public String getOwnReference() {
			return mTrade.getOwnReference();
		}

		@Override
		public double getPrice() {
			return mTrade.getPrice();
		}

		@Override
		public String getReason() {
			return "";
		}

		@Override
		public String getStrategyId() {
			return mTrade.getStrategyId();
		}

		@Override
		public String getSymbol() {
			return mTrade.getSymbol();
		}

		@Override
		public long getTime() {
			return mTrade.getCreateTime();
		}

		@Override
		public String getTimeCondition() {
			return "";
		}

		@Override
		public String getTooltipText() {
			return
				"TRADE "
				+ getSymbol() + " "
				+ (getAction().equals(OrderTradeAction.Buy.toString()) ? "+" : "-") + mVolumeFormatter.format(getVolume()) + " x " 
				+ mPriceFormatter.format(getPrice()) 
				+ " " + getStrategyId()
				;
		}

		@Override
		public TYPE getType() {
			return TYPE.OWN_TRADE;
		}

		@Override
		public int getVolume() {
			return mTrade.getVolume();
		}
		
		@Override
		public String getVolumeAsString() {
			return mVolumeFormatter.format(getVolume());
		}

		@Override
		public boolean isActive() {
			return true;
		}
		
		@Override
		public String getStateAsString() {
			return "";
		}

		@Override
		public String toString(SimpleDateFormat pTimeFormatter,
				NumberFormat pPriceFormatter, NumberFormat pVolumeFormatter) {
			return getTooltipText();
		}

		@Override
		public String getOrderType() {
			return null;
		}
		
	}
}
