package com.fsh.topgui.models;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import com.ev112.codeblack.atc.ATCMain;
import com.ev112.codeblack.atc.PositionBox;
import com.ev112.codeblack.atc.connections.PriceCollectorEventHandler;
import com.ev112.codeblack.atc.connections.StrategyServerConnection;
import com.ev112.codeblack.atc.lookandfeel.AtcFormatters;
import com.ev112.codeblack.common.generated.messages.NameValuePair;
import com.ev112.codeblack.common.generated.messages.PLStrategy;
import com.ev112.codeblack.common.generated.messages.PriceCollectorClockPulseBdx;
import com.ev112.codeblack.common.generated.messages.PriceCollectorFacilityBdx;
import com.ev112.codeblack.common.generated.messages.PriceCollectorQuoteBdx;
import com.ev112.codeblack.common.generated.messages.PriceCollectorReplayStartedBdx;
import com.ev112.codeblack.common.generated.messages.PriceCollectorReplayStoppedBdx;
import com.ev112.codeblack.common.generated.messages.PriceCollectorTradeBdx;
import com.ev112.codeblack.common.generated.messages.StatusEvent;
import com.ev112.codeblack.common.generated.messages.StrategyServer_OwnTrade;
import com.ev112.codeblack.common.instmodel.Instrument;
import com.ev112.codeblack.common.instmodel.InstrumentModel;
import com.ev112.codeblack.common.ordercontroller.OrderTradeAction;
import com.ev112.codeblack.common.strategy.StrategyStatus.STRATEGY_STATUS;
import com.ev112.codeblack.common.strategy.StrategyStatus.STRATEGY_TYPE;
import com.ev112.codeblack.common.strategy.strategies.buckets.Position;
import com.ev112.codeblack.workbench.gui.tools.StaticColumnData;
import com.fsh.topgui.framework.AlphaTableRenderer;

/**
 * Displays details about a strategies
 */
public class StrategyDetailTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	// Table
	final String mId;
	JTable mTable;
	JScrollPane mTablePane;
	
	// Renderer
	private final AlphaTableRenderer mResultRenderer;
	
	// Strategy list
	private final ArrayList<StrategyData> mStrategyList = new ArrayList<StrategyData>();
	
	// Connection 
	private final StrategyServerConnection mMgmtConnection;
	
	// Position Model
//	private final PositionTableModel mPositionModel;
	
	// Open/close position box
	private PositionBox mPositionBox = null;
	
	// Define columns
	private final StaticColumnData[] mColumnData = {
		  new StaticColumnData("Strategy", 80, JLabel.LEFT, String.class, null, null)
		, new StaticColumnData("Status", 50, JLabel.LEFT, String.class, null, null)
		, new StaticColumnData("# Trds", 50, JLabel.RIGHT, String.class, null, null)
//		, new StaticColumnData("State", 50, JLabel.LEFT, String.class, null, null)
//		, new StaticColumnData("Entry Time", 50, JLabel.LEFT, String.class, null, null)
//		, new StaticColumnData("Substate", 50, JLabel.LEFT, String.class, null, null)
//		, new StaticColumnData("Last Reason", 50, JLabel.LEFT, String.class, null, null)
//		, new StaticColumnData("Value", 50, JLabel.RIGHT, String.class, null, null)
//		, new StaticColumnData("TP", 50, JLabel.RIGHT, String.class, null, null)
//		, new StaticColumnData("CL", 50, JLabel.RIGHT, String.class, null, null)
//		, new StaticColumnData("Break", 50, JLabel.LEFT, String.class, null, null)
//		, new StaticColumnData("Pos", 50, JLabel.RIGHT, String.class, null, null)
//		, new StaticColumnData("MPL", 50, JLabel.RIGHT, String.class, null, null)
//		, new StaticColumnData("UPL", 50, JLabel.RIGHT, String.class, null, null)
		, new StaticColumnData("P&L", 50, JLabel.RIGHT, String.class, null, null)

	};
	
	/**
	 * Constructor
	 * @param pInstrumentData
	 * @param pDate
	 */
	public StrategyDetailTableModel(StrategyServerConnection pConnection, String pId, final Window pOwner/*, PositionTableModel pPositionModel*/) {
		
		mMgmtConnection = pConnection;
//		mPositionModel = pPositionTableModel;
		mId = pId;
		mResultRenderer = new AlphaTableRenderer(mColumnData);
		
		mTable = new JTable(this);
		mTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mTable.setGridColor(Color.BLACK);
		mTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// Set table attributes
		StaticColumnData.setColumnAttributes(mColumnData, mTable, mId, mResultRenderer, null);
		mResultRenderer.setColumnAttributes(mTable.getColumnModel(), mColumnData);
		mTable.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
		mTable.getTableHeader().setDefaultRenderer(mResultRenderer);
		
		// Create popup menu
		final JPopupMenu tMenu = new JPopupMenu();
		final JMenuItem tStrategyItem = new JMenuItem();
		tMenu.add(tStrategyItem);
		tStrategyItem.setEnabled(false);
		tMenu.addSeparator();
				
		final JMenuItem tStartItem = new JMenuItem("Load Strategy"); 
		tMenu.add(tStartItem);
		tStartItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				loadStrategy(tStrategyItem.getText());
			}
		});
		
		final JMenuItem tStopItem = new JMenuItem("Unload Strategy"); 
		tMenu.add(tStopItem);
		tStopItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				unloadStrategy(tStrategyItem.getText());
			}
		});
		
//		tMenu.addSeparator();
		
//		final JMenuItem tOrderItem = new JMenuItem("Order Entry");
//		tMenu.add(tOrderItem);
//		tOrderItem.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				for (StrategyData tData : mStrategyList) {
//					if (tStrategyItem.getText().equals(tData.getStrategyId())) {
//						ATC_SE_Strategy tStrategy = tData.getStrategy();
//						if (mOrderBox == null) {
//							mOrderBox = new EnterOrderBox(pOwner);
//						}
//						mOrderBox.showDialog(tStrategy, null);
//						return;
//					}
//				}
//			}
//		});
//		
//		final JMenuItem tOpenPositionItem = new JMenuItem("Request Position");
//		tMenu.add(tOpenPositionItem);
//		tOpenPositionItem.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				for (StrategyData tData : mStrategyList) {
//					if (tStrategyItem.getText().equals(tData.getStrategyId())) {
//						ATC_SE_Strategy tStrategy = tData.getStrategy();
//						if (mPositionBox == null) {
//							mPositionBox = new PositionBox(pOwner);
//						}
//						mPositionBox.showDialog(tStrategy, null, OPERATION.REQUEST);
//						return;
//					}
//				}
//			}
//		});
//		
//		final JMenuItem tClosePositionItem = new JMenuItem("Close Position");
//		tMenu.add(tClosePositionItem);
//		tClosePositionItem.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				for (StrategyData tData : mStrategyList) {
//					if (tStrategyItem.getText().equals(tData.getStrategyId())) {
//						ATC_SE_Strategy tStrategy = tData.getStrategy();
//						if (mPositionBox == null) {
//							mPositionBox = new PositionBox(pOwner);
//						}
//						mPositionBox.showDialog(tStrategy, null, OPERATION.CLOSE);
//						return;
//					}
//				}
//			}
//		});
//		
//		tMenu.addSeparator();
//		
//		JMenuItem tShowStatisticsItem = new JMenuItem("Statistics");
//		tMenu.add(tShowStatisticsItem);
//		tShowStatisticsItem.addActionListener(new ActionListener() {			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// Find the strategy selected
//				for (StrategyData tData : mStrategyList) {
//					if (tStrategyItem.getText().equals(tData.getStrategyId())) {
//						ATCMain.getInstance().getPlotPanel().displayComponent(
//							tData.getStatisticsComponent(), tData.getStrategyId() + " Statistics");
//						return;
//					}
//				}
//			}
//		});
//		
//		JMenuItem tShowParameterItem = new JMenuItem("Parameters");
//		tMenu.add(tShowParameterItem);
//		tShowParameterItem.addActionListener(new ActionListener() {			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// Find the strategy selected
//				for (StrategyData tData : mStrategyList) {
//					if (tStrategyItem.getText().equals(tData.getStrategyId())) {
//						ATCMain.getInstance().getPlotPanel().displayComponent(
//							tData.getParameterComponent(), tData.getStrategyId() + " Parameters");
//						return;
//					}
//				}
//			}
//		});
//		
//		JMenuItem tStateHistoryItem = new JMenuItem("State History");
//		tMenu.add(tStateHistoryItem);
//		tStateHistoryItem.addActionListener(new ActionListener() {			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// Find the strategy selected
//				for (StrategyData tData : mStrategyList) {
//					if (tStrategyItem.getText().equals(tData.getStrategyId())) {
//						ATCMain.getInstance().getPlotPanel().displayComponent(
//							tData.getStateHistoryComponent(), tData.getStrategyId() + " State History");
//						return;
//					}
//				}
//			}
//		});
//		
		// Add mouse listener and show popup
		mTable.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					int tRow = mTable.rowAtPoint(e.getPoint());
					StrategyData tData = mStrategyList.get(tRow);
					tStrategyItem.setText(tData.getStrategyId());
					tMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});		
	}
	
	/**
	 * Send "load" to strategy server
	 * @param pStrategyId
	 */
	private void loadStrategy(String pStrategyId) {
		mMgmtConnection.loadStrategy(pStrategyId);
	}
	
	/**
	 * Load all strategies
	 */
	public void loadAllStrategies() {
		for (StrategyData s : mStrategyList) {
			mMgmtConnection.loadStrategy(s.getStrategyId());
		}
	}
	
	/**
	 * Send "unload" to strategy server
	 * @param pStrategyId
	 */
	public void unloadStrategy(String pStrategyId) {
		mMgmtConnection.unloadStrategy(pStrategyId);
	}
	
	/**
	 * Unload all strategies
	 */
	public void unloadAllStrategies() {
		for (StrategyData s : mStrategyList) {
			mMgmtConnection.unloadStrategy(s.getStrategyId());
		}
	}
	
	/**
	 * Get JTable -- create a JTable mapped to this model
	 * <br>
	 * Yes this kind of breaks model view paradigm, but it is convenient...
	 */
	public JComponent getComponent(final Window pOwner) {
		return mTable;
	}
	
	@Override
	public int getColumnCount() {
		return mColumnData.length;
	}
	
	@Override
	public String getColumnName(int pCol) {
		return mColumnData[pCol].mHeader;
	}

	@Override
	public int getRowCount() {
		return mStrategyList.size() + 1;	// 1 for "TOTAL" row
	}

	/**
	 * Format is rows with SYMBOL NET_POS MPL Price UPL P&L
	 * 1st column is label column
	 */
	@Override
	public Object getValueAt(int pRow, int pCol) {
		
		if (pRow < mStrategyList.size()) {

			StrategyData tStrategy = mStrategyList.get(pRow);
			StateHistoryEntry tState = tStrategy.mStateModel.getLastEntry();
			
			switch (pCol) {
				case 0: // Strategy
					return tStrategy.getStrategyId();
					
				case 1: // Status
					return tStrategy.mLoaded ? "Loaded" : "Not Loaded";
					
				case 2: // Nr trades
					return 0; // return Integer.toString(mPositionModel.getNrTradesForStrategy(tStrategy.getStrategyId()));
					
				case 3: // P&L
					return 0d; // return AtcFormatters.getInstance().getPLFormatter().format(mPositionModel.getPandLForStrategy(tStrategy.getStrategyId()));
					
	//			case 2: // State
	//				return tState != null ? tState.mState : null;
	//				
	//			case 3: // State entry time
	//				return tState != null ? tState.mTime : null;
	//				
	//			case 4: // Substate
	//				return tState != null ? tState.mSubState : null;
	//				
	//			case 5: // Last reason
	//				return tState != null ? tState.mLastReason : null;
	//				
	//			case 6: // Value
	//				return tStrategy.mValue;
	//				
	//			case 7: // Take profit
	//				return tStrategy.mTakeProfitPrice;
	//				
	//			case 8: // Cut loss
	//				return tStrategy.mCutLossPrice;
	//				
	//			case 9: // Break time
	//				return tStrategy.mReportTime;
	//				
	//			case 10: // Position
	//				return tStrategy.getPosition();
	//				
			}
		} else {
			
			// Total row
			if (pCol == 0) {
				return "TOTAL";
			} else if (pCol == 2) {
				return "0"; // return Integer.toString(mPositionModel.getNrTradesTotal());
			} else if (pCol == 3) {
				return ""; // return AtcFormatters.getInstance().getPLFormatter().format(mPositionModel.getPandLTotal());
			}
			
		}
		return null;
	}
	
	@Override
	public Class<?> getColumnClass(int pCol) {
		return String.class;
	}
	
	/**
	 * Incoming strategy broadcasts come here
	 */
	public void updateStrategy(PLStrategy pStrategy) {

		// Tell Position Model
		
/*		
		for (PLUnit tUnit : pStrategy.getPlUnits()) {
			mPositionModel.updatePositions(tUnit.getPositions());
		}
*/
		
		// Work on strategy level
		for (int i = 0; i < mStrategyList.size(); i++) {
			StrategyData tData = mStrategyList.get(i);
			if (tData.getStrategyId().equals(pStrategy.getStrategyId())) {
				
				// Found
				tData.updateStrategy(pStrategy);
				fireTableRowsUpdated(i, i);
				return;
			}
		}
		
		// Come here if we didn't find the strategy -- it's a new one
		StrategyData tData = new StrategyData(pStrategy);
		mStrategyList.add(tData);
		fireTableRowsInserted(mStrategyList.size() - 1, mStrategyList.size() - 1);
		
	}
	
	/** 
	 * New own trade 
	 */
	public void addTrade(StrategyServer_OwnTrade pTrade) {
		for (StrategyData tData : mStrategyList) {
			if (tData.getStrategyId().equals(pTrade.getStrategyId())) {
				tData.addTrade(pTrade);
			}
		}
	}
	

	
	/**
	 * Save preferences
	 */
	public void savePreferences(Preferences pPrefs) {
		StaticColumnData.savePreferences(pPrefs, mColumnData, mTable, mId);
		for (StrategyData tData : mStrategyList) {
			tData.savePreferences(pPrefs);
		}
	}
	
	/**
	 * Helper class to wrap all strategy data
	 */
	class StrategyData implements PriceCollectorEventHandler {
		
		private PLStrategy mStrategy;

		private boolean mLoaded;
		private String mValue;
		private String mTakeProfitPrice;
		private String mCutLossPrice;
		private String mReportTime;
		private String mPandL;
		private String mPos;

		private StateHistoryTableModel mStateModel;
		private ParameterTableModel mParamModel;
		private StatisticsTableModel mStatisticsModel;
		
		private JScrollPane mStateComponent;
		private JScrollPane mParameterComponent;
		private JScrollPane mStatisticsComponent;
		
		private Map<String,Position> mPositions = new HashMap<String, Position>();
		private ArrayList<StrategyServer_OwnTrade> mOwnTrades = new ArrayList<StrategyServer_OwnTrade>();
		

		/**
		 * Create it from a strategy data instance
		 * @param pStrategyData
		 */
		StrategyData(PLStrategy pStrategyData) {
			mStrategy = pStrategyData;
			mLoaded = pStrategyData.getLoaded();
			//mStartTime = removeMilliSeconds(pStrategyData.getStartTime());
			
			mStateModel = new StateHistoryTableModel(mStrategy.getStrategyId());
			mStateComponent = new JScrollPane(mStateModel.getComponent());
			
			mParamModel = new ParameterTableModel();
			mParameterComponent = new JScrollPane(/* mParamModel.getComponent() */);
			
			mStatisticsModel = new StatisticsTableModel();
			mStatisticsComponent = new JScrollPane(mStatisticsModel.getComponent());

			mValue = "";
			mTakeProfitPrice = "";
			mCutLossPrice = "";
			mReportTime = "";
			mPandL = "";
			mPos = "";
			
			updateStrategy(pStrategyData);
		}
		
		String getTooltipText() {
			return "TBS";
		}
		
		PLStrategy getStrategy() {
			return mStrategy;
		}
		
		String getStrategyId() {
			return mStrategy.getStrategyId();
		}
		
		Component getStateHistoryComponent() {
			return mStateComponent;
		}
		
		Component getParameterComponent() {
			return mParameterComponent;
		}
		
		Component getStatisticsComponent() {
			return mStatisticsComponent;
		}
		
		void updateStrategy(PLStrategy pStrategy) {
			
			// Timestamps
//			mStatisticsModel.addParameterEntry("Start Time", removeMilliSeconds(pStrategy.getStartTime()));
//			mStatisticsModel.addParameterEntry("Poll Time", removeMilliSeconds(pStrategy.getPollTime()));
			mLoaded = pStrategy.getLoaded();
			
//			// Handle position updates
//			if (pStrategy.getParameters() != null) {
//				for (NameValuePair tPair : pStrategy.getParameters()) {
//					if (tPair.getSource().equals(STRATEGY_SOURCE.PARAMETERS.toString())) {
//						mParamModel.addParameterEntry(getParameterName(tPair), getParameterValue(tPair));
//					} else if (!tPair.getSource().equals(STRATEGY_SOURCE.STATE.toString())) {
//						mStatisticsModel.addParameterEntry(getParameterName(tPair), getParameterValue(tPair));
//					}
//				}
//				
//				// Update state model also
//				mStateModel.updateState(pStrategy.getParameters());
//				
//				// Finally take care of "our" "global" state parameters
//				for (NameValuePair tPair : pStrategy.getParameters()) {
//					if (tPair.getSource().equals(STRATEGY_SOURCE.STATE.toString())) {
//						if (tPair.getName().equals(STRATEGY_STATUS.RSI_VALUE.toString())) {
//							mValue = StrategyDetailedModel.getParameterValue(tPair);
//						} else if (tPair.getName().equals(STRATEGY_STATUS.TP_MA_DIFF.toString())) {
//							mValue = StrategyDetailedModel.getParameterValue(tPair);
//						} else if (tPair.getName().equals(STRATEGY_STATUS.PRIVATE_INFO.toString())) {
//							mValue = StrategyDetailedModel.getParameterValue(tPair);
//						} else if (tPair.getName().equals(STRATEGY_STATUS.TAKE_PROFIT_PRICE.toString())) {
//							mTakeProfitPrice = StrategyDetailedModel.getParameterValue(tPair);
//						} else if (tPair.getName().equals(STRATEGY_STATUS.CUT_LOSS_PRICE.toString())) {
//							mCutLossPrice = StrategyDetailedModel.getParameterValue(tPair);
//						} else if (tPair.getName().equals(STRATEGY_STATUS.REPORT_TIME.toString())) {
//							String s = StrategyDetailedModel.getParameterValue(tPair);
//							if (s.equals("0")) {
//								mReportTime = "";
//							} else {
//								mReportTime = StrategyDetailedModel.getParameterValue(tPair);
//							}
//						}
//					}
//				}
//			}
//			
//			// Add/remove positions
//			if (pStrategy.getInstruments() != null) {
//				for (String s : pStrategy.getInstruments()) {
//					getPosition(s); // Creates the position
//				}
//			}
//			
		}
		
		String removeMilliSeconds(String s) {
			int i = s.indexOf('.');
			return i < 1 ? s : s.substring(0, i);
		}
		
		int getRowCount() {
			return mColumnData.length;
		}
		
		void savePreferences(Preferences pPrefs) {
			mStateModel.savePreferences(pPrefs);
		}

		@Override
		public void priceCollectorTradeEvent(PriceCollectorTradeBdx pBdx) {
			Position tPos = getPosition(pBdx.getSymbol());
			if (tPos != null) {
				tPos.registerTrade(pBdx.getTime(), pBdx.getPrice());
			}
			fireTableDataChanged();
		}
		
		void addTrade(StrategyServer_OwnTrade pTrade) {
			Position tPos = getPosition(pTrade.getSymbol());
			if (tPos == null) {
				mOwnTrades.add(pTrade);
			} else {
				int tSize = pTrade.getVolume() * (pTrade.getAction().equals(OrderTradeAction.Buy.toString()) ? 1 : -1);
				tPos.addOwnTrade(pTrade.getCreateTime(), tSize, pTrade.getPrice());
			}
			fireTableDataChanged();
		}
		
		double getMPL() {
			double tMPL = 0d;
			for (Position tPos : mPositions.values()) {
				tMPL += tPos.getMPL();
			}
			return tMPL;
		}

		double getUPL() {
			double tUPL = 0d;
			for (Position tPos : mPositions.values()) {
				tUPL += tPos.getUPL();
			}
			return tUPL;
		}

		double getPandL() {
			double tPandL = 0d;
			for (Position tPos : mPositions.values()) {
				tPandL += tPos.getPandL();
			}
			return tPandL;
		}
		
		String getPosition() {
			StringBuilder tStr = new StringBuilder();
			int i = 0;
			for (Position tPos : mPositions.values()) {
				if (i > 0) {
					tStr.append(", ");
				}
				//tStr.append(Integer.toString(tPos.getNetSize()));
				tStr.append(tPos.getSymbol() + " ");
				tStr.append("Bought:" + Integer.toString(tPos.getBuySize()) + " ");
				tStr.append("Sold:" + Integer.toString(tPos.getSellSize()) + " ");
			}
			return tStr.toString();
		}
		
		Position getPosition(String pSymbol) {
			Position tPos = mPositions.get(pSymbol);
			if (tPos == null) {
				InstrumentModel tModel = ATCMain.getInstance().getInstrumentModel(); 
				Instrument tInstr = tModel != null ? tModel.getInstrument(pSymbol) : null;
				if (tInstr != null) {
					tPos = new Position(tInstr);
					mPositions.put(pSymbol, tPos);

					// Push down all own trades we may have received
					for (StrategyServer_OwnTrade tTrade : mOwnTrades) {
						int tSize = tTrade.getVolume() * (tTrade.getAction().equals(OrderTradeAction.Buy.toString()) ? 1 : -1);
						tPos.addOwnTrade(tTrade.getCreateTime(), tSize, tTrade.getPrice());
					}
					mOwnTrades.clear(); // Not needed any more
					
					// Set up price subscription
					ATCMain.getInstance().getServerModel().getPriceCollectorConnection().subscribeTrades(pSymbol, this);
				}
			}			
			return tPos;
		}
		
		boolean hasPosition() {
			for (Position tPos : mPositions.values()) {
				if (tPos.getNetSize() != 0) {
					return true;
				}
			}
			return false;
		}

		@Override
		public void priceCollectorQuoteEvent(PriceCollectorQuoteBdx pBdx) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void priceCollectorClockPulseEvent(PriceCollectorClockPulseBdx pBdx) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void facilityUpdate(PriceCollectorFacilityBdx pBdx) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void priceCollectorStatusEvent(List<StatusEvent> pEvents) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void priceCollectorReplayStarted(PriceCollectorReplayStartedBdx pBdx) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void priceCollectorReplayStopped(PriceCollectorReplayStoppedBdx pBdx) {
			// TODO Auto-generated method stub
			
		}

	}
		
	public static String getParameterName(NameValuePair pPair) {
		for (STRATEGY_STATUS tStatus : STRATEGY_STATUS.values()) {
			if (tStatus.name().equals(pPair.getName())) {
				return tStatus.getLabel();
			}
		}
		return pPair.getName();
	}
	
	public static String getParameterValue(NameValuePair pPair) {
		if (pPair.getType() != null) {
			for (STRATEGY_TYPE tType : STRATEGY_TYPE.values()) {
				if (tType.name().equals(pPair.getType())) {
					switch (tType) {
						case TIME_MILLIS:
							try {
								Long tTime = new Long(pPair.getValue());
								if (tTime == 0L) {
									return "";
								}
								return AtcFormatters.getInstance().getTimeFormatter().format(tTime);
							} catch (NumberFormatException e) {
								return pPair.getValue();
							}
							
						default:
							return pPair.getValue();
					}
				}
			}
		}
		return pPair.getValue();
	}
	
	class OurRenderer extends AlphaTableRenderer {
		private static final long serialVersionUID = 1L;
		
		public OurRenderer(StaticColumnData[] pColumnData) {
			super(pColumnData);
		}
		
		@Override
		public Color getTableForegroundColor(int pRow) {
			return super.getTableForegroundColor(pRow);
		}

		@Override
		public Color getTableBackgroundColor(int pRow) {
			Color rowColor = null;
			if (pRow >= 0) {
				Color profit = new Color(140, 209, 216);
				Color loss = new Color(207, 149, 161);

				if (pRow < mStrategyList.size()) {
					StrategyData tData = mStrategyList.get(pRow);
					double pl = 0d; // mPositionModel.getPandLForStrategy(tData.getStrategyId());
					if (pl > 0d) {
						rowColor = profit;
					} else if (pl < 0d) {
						rowColor = loss;
					} 
				}
			}
			
			if (rowColor != null) {
				return rowColor;
			}

			// Header row
			return super.getTableBackgroundColor(pRow);
		}

	}

}

