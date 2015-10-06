package com.fsh.topgui.models;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.prefs.Preferences;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

import com.ev112.codeblack.atc.connections.PriceCollectorConnectionEventHandler;
import com.ev112.codeblack.atc.connections.StrategyServerConnection;
import com.ev112.codeblack.atc.lookandfeel.AtcFormatters;
import com.ev112.codeblack.common.generated.messages.PLPosition;
import com.ev112.codeblack.common.generated.messages.PLUnitMgmtReq;
import com.ev112.codeblack.common.generated.messages.PriceCollectorClockPulseBdx;
import com.ev112.codeblack.common.generated.messages.PriceCollectorFacilityBdx;
import com.ev112.codeblack.common.generated.messages.PriceCollectorQuoteBdx;
import com.ev112.codeblack.common.generated.messages.PriceCollectorReplayStartedBdx;
import com.ev112.codeblack.common.generated.messages.PriceCollectorReplayStoppedBdx;
import com.ev112.codeblack.common.generated.messages.PriceCollectorTradeBdx;
import com.ev112.codeblack.common.generated.messages.StatusEvent;
import com.ev112.codeblack.common.generated.messages.StrategyServer_OwnTrade;
import com.ev112.codeblack.workbench.gui.tools.StaticColumnData;
import com.fsh.topgui.framework.AlphaTableRenderer;

/**
 * Displays details about a strategies
 */
public class PositionTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	// Table
	final String mId;
	final JPanel mPanel;
	final JTable mTable;
	
	// Strategy combo box
	final JComboBox<String> mStrategySelector = new JComboBox<String>();
	final String mAllOption = "All";

	// Display selectors
	final JCheckBox mAllBox = new JCheckBox("All", true);
	final JCheckBox mStrategyBox = new JCheckBox("Strategy", false);
	final JCheckBox mNonZeroPosBox = new JCheckBox("Non zero pos", true);
	final JCheckBox mNonZeroPLBox = new JCheckBox("Non zero p&l", true);

	
	// Renderer
	private final OurRenderer mResultRenderer = new OurRenderer();
	
	// Strategy list
	private final ArrayList<PositionData> mPositionList = new ArrayList<PositionData>();
	
	// Display list (filtered from mPositionList)
	private final ArrayList<PositionData> mDisplayList = new ArrayList<PositionData>();
	
	// Connection 
	private final StrategyServerConnection mMgmtConnection;
	
	// Define columns
	private final StaticColumnData[] mColumnData = {
		  new StaticColumnData("Selected", 80, JLabel.CENTER, Boolean.class, null, null)	
		, new StaticColumnData("Strategy", 80, JLabel.LEFT, String.class, mResultRenderer, null)
		, new StaticColumnData("PL Unit", 50, JLabel.LEFT, String.class, mResultRenderer, null)
		, new StaticColumnData("Status", 50, JLabel.LEFT, String.class, mResultRenderer, null)
		, new StaticColumnData("Symbol", 50, JLabel.LEFT, String.class, mResultRenderer, null)
		, new StaticColumnData("Strategy State", 50, JLabel.LEFT, String.class, mResultRenderer, null)
		, new StaticColumnData("XM State", 50, JLabel.LEFT, String.class, mResultRenderer, null)
		, new StaticColumnData("# trades", 50, JLabel.RIGHT, String.class, mResultRenderer, null)
		, new StaticColumnData("Pos", 50, JLabel.RIGHT, String.class, mResultRenderer, null)
		, new StaticColumnData("MPL", 50, JLabel.RIGHT, String.class, mResultRenderer, null)
		, new StaticColumnData("UPL", 50, JLabel.RIGHT, String.class, mResultRenderer, null)
		, new StaticColumnData("Cost", 50, JLabel.RIGHT, String.class, mResultRenderer, null)
		, new StaticColumnData("P&L", 50, JLabel.RIGHT, String.class, mResultRenderer, null)
		, new StaticColumnData("Strategy Info", 150, JLabel.LEFT, String.class, mResultRenderer, null)
	};
	
	/**
	 * Constructor
	 * @param pInstrumentData
	 * @param pDate
	 */
	public PositionTableModel(StrategyServerConnection pConnection, String pId, final Window pOwner) {
		mPanel = new JPanel(new BorderLayout());
		
		mMgmtConnection = pConnection;
		mId = pId;
		mResultRenderer.setColumnData(mColumnData);
		
		mTable = new JTable(this);
		mTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mTable.setGridColor(Color.BLACK);
		mTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		mTable.setBackground(ATCMain.getInstance().getGuiEnvironment().getTableBackgroundColor());
//		mTable.setAutoCreateRowSorter(true); // TODO GL In order to get this going, need to create a TableRowSorter, handle null etc

		// Set table attributes
		StaticColumnData.setColumnAttributes(mColumnData, mTable, mId, null, null);
		mResultRenderer.setColumnAttributes(mTable.getColumnModel(), mColumnData);
		mTable.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
		mTable.getTableHeader().setDefaultRenderer(mResultRenderer);
		
		JCheckBox tSelectBox = new JCheckBox();
		tSelectBox.setSelected(false);
		
		// Create popup menu
		final JPopupMenu tMenu = new JPopupMenu();
		
		final PositionMenuItem tNameItem = new PositionMenuItem();
		tMenu.add(tNameItem);
		
		tMenu.addSeparator();

		final JMenuItem tStartItem = new JMenuItem("Start"); 
		tMenu.add(tStartItem);
		tStartItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PositionData p = tNameItem.mPositionData;
				mgmtStrategy(p.getStrategyId(), p.getPLUnitId(), PLUnitMgmtReq.START);
			}
		});
		
		final JMenuItem tStopItem = new JMenuItem("Halt"); 
		tMenu.add(tStopItem);
		tStopItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PositionData p = tNameItem.mPositionData;
				mgmtStrategy(p.getStrategyId(), p.getPLUnitId(), PLUnitMgmtReq.HALT);
			}
		});
		
		final JMenuItem tShutItem = new JMenuItem("Shut"); 
		tMenu.add(tShutItem);
		tShutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PositionData p = tNameItem.mPositionData;
				mgmtStrategy(p.getStrategyId(), p.getPLUnitId(), PLUnitMgmtReq.SHUT);
			}
		});
		
		// For identifying popup
		mTable.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3 || e.getClickCount() == 2) {
					int tRow = mTable.convertRowIndexToModel(mTable.rowAtPoint(e.getPoint()));
					int tCol = mTable.convertColumnIndexToModel(mTable.columnAtPoint(e.getPoint()));
					if (tRow >= 0 && tCol > 0 && mDisplayList.get(tRow) instanceof PositionData) {
						PositionData tOrderData = mDisplayList.get(tRow);
						tNameItem.setPositionData(tOrderData);
						tMenu.show(e.getComponent(), e.getX(), e.getY());
					}
				}
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
//		JMenuItem tStateHistoryItem = new JMenuItem("State History");
//		tMenu.add(tStateHistoryItem);
//		tStateHistoryItem.addActionListener(new ActionListener() {			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// Find the strategy selected
//				for (StrategyData tData : mPositionList) {
//					if (tStrategyItem.getText().equals(tData.getStrategyId())) {
//						ATCMain.getInstance().getPlotPanel().displayComponent(
//							tData.getStateHistoryComponent(), tData.getStrategyId() + " State History");
//						return;
//					}
//				}
//			}
//		});
		
		// Add mouse listener and show popup
		mTable.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					int tRow = mTable.convertRowIndexToModel(mTable.rowAtPoint(e.getPoint()));
					PositionData tPos = mDisplayList.get(tRow);
					tNameItem.setText(tPos.getPLUnitId());
					tMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});		
		
		// Add table to panel
		mPanel.add(new JScrollPane(mTable), BorderLayout.CENTER);
		
		// Command line at bottom
		// Strategy: <Strategy selector> Select Clear // TODO GL Maybe add Market as filter
		JPanel tCmdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		mPanel.add(tCmdPanel, BorderLayout.PAGE_END);

		// Strategy
		tCmdPanel.add(new JLabel("Strategy"));
		tCmdPanel.add(mStrategySelector);
		updateStrategyComboBox();
		mStrategySelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mStrategyBox.isSelected()) {
					updateDisplayList();
				}
			}
		});
		
		// Select, Clear
		final JButton tSelectBtn = new JButton("Select");
		tCmdPanel.add(tSelectBtn);		
		final JButton tClearBtn = new JButton("Clear");
		tCmdPanel.add(tClearBtn);

		
		ActionListener selectClearListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = (String) mStrategySelector.getSelectedItem();
				JButton b = (JButton) e.getSource();
				for (PositionData p : mPositionList) {
					if (s.equals(mAllOption) || p.getStrategyId().equals(s)) {
						if (b == tSelectBtn) {
							p.setSelected();
						} else {
							p.clearSelected();
						}
					}
				}
				fireTableDataChanged();
			}
		};
		
		tSelectBtn.addActionListener(selectClearListener);
		tClearBtn.addActionListener(selectClearListener);
		
		// Display parts
		
		// Add checkboxes
		tCmdPanel.add(mAllBox);
		tCmdPanel.add(mStrategyBox);
		tCmdPanel.add(mNonZeroPosBox);
		tCmdPanel.add(mNonZeroPLBox);
		
		ActionListener displayFilterListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				updateDisplayList();
			}
		};
		
		mAllBox.addActionListener(displayFilterListener);
		mNonZeroPosBox.addActionListener(displayFilterListener);
		mNonZeroPLBox.addActionListener(displayFilterListener);
		
		// START, SHUT, HALT
		final JButton tStartButton = new JButton("START");
		final JButton tShutButton = new JButton("SHUT");
		final JButton tHaltButton = new JButton("HALT");
		tCmdPanel.add(tStartButton);
		tCmdPanel.add(tShutButton);
		tCmdPanel.add(tHaltButton);
		
		ActionListener cmdListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton b = (JButton) e.getSource();
				int tFunc;
				if (b == tStartButton) {
					tFunc = PLUnitMgmtReq.START;
				} else if (b == tShutButton) {
					tFunc = PLUnitMgmtReq.SHUT;
				} else {
					tFunc = PLUnitMgmtReq.HALT;
				}
				
				for (PositionData p : mDisplayList) {
					if (p.isSelected()) {
						mgmtStrategy(p.getStrategyId(), p.getPLUnitId(), tFunc);
					}
				}
				
				setClearAllSelections(false);
				fireTableDataChanged();
			}
		};
		
		tStartButton.addActionListener(cmdListener);
		tShutButton.addActionListener(cmdListener);
		tHaltButton.addActionListener(cmdListener);
		
	}
	
	/**
	 * Update strategy combo box
	 */
	void updateStrategyComboBox() {
		mStrategySelector.removeAllItems();
		mStrategySelector.addItem(mAllOption);
		Set<String> tStrategyIds = new TreeSet<String>();
		for (PositionData p : mPositionList) {
			tStrategyIds.add(p.getStrategyId());
		}
		for (String s : tStrategyIds) {
			mStrategySelector.addItem(s);
		}
	}
	
	/**
	 * Update display list
	 */
	void updateDisplayList() {
		mDisplayList.clear();
		
		for (PositionData p : mPositionList) {
			if (mAllBox.isSelected()) {
				mDisplayList.add(p);
			} else if (mNonZeroPosBox.isSelected() && p.getNetPosition() != 0) {
				mDisplayList.add(p);
			} else if (mNonZeroPLBox.isSelected() && p.getTotalPL() != 0d) {
				mDisplayList.add(p);
			} else if (mStrategyBox.isSelected() && p.getStrategyId().equals(mStrategySelector.getSelectedItem())) {
				mDisplayList.add(p);
			}
		}
		
		fireTableDataChanged();
	}
	
	/**
	 * Send "start/stop" to strategy server for pl unit
	 */
	private void mgmtStrategy(String pStrategyId, String pUnitId, int pFunction) {
		String [] tUnits = new String[1];
		tUnits[0] = pUnitId;
		mMgmtConnection.plUnitMgmt(pStrategyId, false, tUnits, pFunction);
	}
	
	/**
	 * Get JTable -- create a JTable mapped to this model
	 * <br>
	 * Yes this kind of breaks model view paradigm, but it is convenient...
	 */
	public JComponent getComponent(final Window pOwner) {
		return mPanel;
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
	public Class<?> getColumnClass(int pCol) {
		return mColumnData[pCol].mClass;
	}
	
	@Override
	public boolean isCellEditable(int pRow, int pCol) {
		return pCol == 0;
	}
	
	@Override
	public void setValueAt(Object pValue, int pRow, int pCol) {
		if (pCol == 0) {
			PositionData p = mDisplayList.get(mTable.convertRowIndexToModel(pRow));
			Boolean b = (Boolean) pValue;
			if (b) {
				p.setSelected();
			} else {
				p.clearSelected();
			}
		}
	}

	@Override
	public int getRowCount() {
		return mDisplayList.size();
	}

	/**
	 * Format is rows with SYMBOL NET_POS MPL Price UPL P&L
	 * 1st column is label column
	 */
	@Override
	public Object getValueAt(int pRow, int pCol) {
		
		PositionData tPos = mDisplayList.get(mTable.convertRowIndexToModel(pRow));
//		StateHistoryEntry tState = tStrategy.mStateModel.getLastEntry();
		
		switch (pCol) {
		
			case 0:
				return tPos.isSelected();
						
			case 1: // Strategy
				return tPos.getStrategyId();
				
			case 2: // PL Unit
				return tPos.getPLUnitId();
				
			case 3: // PL Unit State
				return tPos.getPLUnitState();
				
			case 4: // Symbol
				return tPos.getSymbol();
				
			case 5: // Strategy state
				return tPos.getStrategyState();
				
			case 6: // XM State
				return tPos.getXMState();
				
			case 7:	// # of trades
				return Integer.toString(tPos.getNrTrades());
				
			case 8:	// Net position
				return Integer.toString(tPos.getNetPosition());
				
			case 9:	// MPL
				return AtcFormatters.getInstance().getPLFormatter().format(tPos.getMPL());
				
			case 10:	// UPL
				return AtcFormatters.getInstance().getPLFormatter().format(tPos.getUPL());
				
			case 11:	// Cost
				return AtcFormatters.getInstance().getPLFormatter().format(tPos.getCost());
				
			case 12:	// TPL
				return AtcFormatters.getInstance().getPLFormatter().format(tPos.getTotalPL());
				
			case 13:	// Strategy info
				return tPos.getStrategyInfo();
								
		}
		
		return null;
	}
	
	/**
	 * Incoming strategy broadcasts come here
	 */
	public void updatePositions(List<PLPosition> pPositions) {
		for (PLPosition p : pPositions) {
			updatePosition(p);
		}
	}
	
	private void updatePosition(PLPosition pPos) {
		for (int i = 0; i < mPositionList.size(); i++) {
			PositionData tData = mPositionList.get(i);
			boolean tEqual =
				pPos.getStrategyId().equals(tData.getStrategyId())
				&& pPos.getPlUnitId().equals(tData.getPLUnitId()) 
				&& pPos.getSymbol().equals(tData.getSymbol());
				
				// Found
			if (tEqual) {
				tData.updatePosition(pPos);
				updateDisplayList();
				return;
			}
		}
		
		// Come here if we didn't find the position -- it's a new one
		PositionData tData = new PositionData(pPos.getStrategyId(), pPos.getPlUnitId(), pPos.getSymbol());
		mPositionList.add(tData);
		tData.updatePosition(pPos);
		updateDisplayList();
		
		// Also update strategy combo box
		updateStrategyComboBox();
	}
	
	/** 
	 * New own trade 
	 */
	public void addTrade(StrategyServer_OwnTrade pTrade) {
		for (PositionData tData : mPositionList) {
			if (tData.getStrategyId().equals(pTrade.getStrategyId())) {
				tData.addTrade(pTrade);
			}
		}
	}
	
	/**
	 * Set/Clear all selections
	 */
	private void setClearAllSelections(boolean set) {
		for (PositionData p : mPositionList) {
			if (set) {
				p.setSelected();
			} else {
				p.clearSelected();
			}
		}
		fireTableDataChanged();
	}
	
	/**
	 * Save preferences
	 */
	public void savePreferences(Preferences pPrefs) {
		StaticColumnData.savePreferences(pPrefs, mColumnData, mTable, mId);
	}
	
	/**
	 * Helper method to sum p&l for a strategy
	 */
	public double getPandLForStrategy(String pStrategyId) {
		double pl = 0d;
		for (PositionData p : mPositionList) {
			if (p.getStrategyId().equals(pStrategyId)) {
				pl += p.getTotalPL();
			}
		}
		return pl;
	}
	
	/*
	 * Helper method to get the total p&l across strategies
	 */
	public double getPandLTotal() {
		double pl = 0d;
		for (PositionData p : mPositionList) {
			pl += p.getTotalPL();
		}
		return pl;
	}
	
	/**
	 * Helper method to get number of trades for a strategy
	 */
	public int getNrTradesForStrategy(String pStrategyId) {
		int n = 0;
		for (PositionData p : mPositionList) {
			if (p.getStrategyId().equals(pStrategyId)) {
				n += p.getNrTrades();
			}
		}
		return n;		
	}
	
	/**
	 * Helper method to get number of trades for a strategy
	 */
	public int getNrTradesTotal() {
		int n = 0;
		for (PositionData p : mPositionList) {
			n += p.getNrTrades();
		}
		return n;		
	}
	
	/**
	 * Helper class to wrap all strategy data
	 */
	class PositionData implements PriceCollectorConnectionEventHandler {
		final String mStrategyId;
		final String mPLUnitId;
		final String mSymbol;
		
		boolean mSelected = false;
		
		String mPLUnitState = "";
		String mXmState = "";
		
		int mNetPosition = 0;
		
		int mNrTrades = 0;
		
		double mMPL = 0d;
		double mUPL = 0d;
		double mCost = 0d;
		
		String mStrategyState;
		String mStrategyInfo;

//		StateHistoryModel mStateModel;
//		ParameterModel mParamModel;
//		StatisticsModel mStatisticsModel;
		
		JScrollPane mStateComponent;
		JScrollPane mParameterComponent;
		JScrollPane mStatisticsComponent;
		
		ArrayList<StrategyServer_OwnTrade> mOwnTrades = new ArrayList<StrategyServer_OwnTrade>();
		

		/**
		 * Create it from a strategy data instance
		 * @param pStrategyData
		 */
		PositionData(String pStrategyId, String pPLUnitId, String pSymbol) {
			mStrategyId = pStrategyId;
			mPLUnitId = pPLUnitId;
			mSymbol = pSymbol;
			
//			mStateModel = new StateHistoryModel(mStrategy.getStrategyId());
//			mStateComponent = new JScrollPane(mStateModel.getComponent());
//			
//			mParamModel = new ParameterModel();
//			mParameterComponent = new JScrollPane(mParamModel.getComponent());
//			
//			mStatisticsModel = new StatisticsModel();
//			mStatisticsComponent = new JScrollPane(mStatisticsModel.getComponent());

//			updateStrategy(pStrategyData);
		}
		
		String getTooltipText() {
			return "TBS";
		}
		
		String getStrategyId() { return mStrategyId; }
		
		String getPLUnitState() { return mPLUnitState; }
		
		String getPLUnitId() { return mPLUnitId; }
		
		String getSymbol() { return mSymbol; }
		
		boolean isSelected() { return mSelected; }
		
		String getXMState() { return mXmState; }
		
//		Component getStateHistoryComponent() {
//			return mStateComponent;
//		}
//		
//		Component getParameterComponent() {
//			return mParameterComponent;
//		}
//		
//		Component getStatisticsComponent() {
//			return mStatisticsComponent;
//		}
//		
		void updatePosition(PLPosition pPos) {
			
			mPLUnitState = pPos.getPlUnitState();
			mXmState = pPos.getXmState();
			mNetPosition = pPos.getNetPosition();
			mNrTrades = pPos.getNrTrades();
			mMPL = pPos.getMpl();
			mUPL = pPos.getUpl();
			mCost = pPos.getCost();
			mStrategyState = pPos.getStrategyState();
			mStrategyInfo = pPos.getStrategyInfo();
			
			// Timestamps
//			mStatisticsModel.addParameterEntry("Start Time", removeMilliSeconds(pStrategy.getStartTime()));
//			mStatisticsModel.addParameterEntry("Poll Time", removeMilliSeconds(pStrategy.getPollTime()));
//			mLoaded = pStrategy.getLoaded();
			
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
		
		void setSelected() {
			mSelected = true;
		}
		
		void clearSelected() {
			mSelected = false;
		}
		
		String removeMilliSeconds(String s) {
			int i = s.indexOf('.');
			return i < 1 ? s : s.substring(0, i);
		}
		
		int getRowCount() {
			return mColumnData.length;
		}
		
		int getNetPosition() { return mNetPosition; }
		
		int getNrTrades() { return mNrTrades; }
		
		double getMPL() { return mMPL; }
		
		double getUPL() { return mUPL; }
		
		double getCost() { return mCost; }
		
		double getTotalPL() { return mMPL + mUPL - mCost; }
		
		String getStrategyState() { return mStrategyState; }
		
		String getStrategyInfo() { return mStrategyInfo; }
		
		void savePreferences(Preferences pPrefs) {
//			mStateModel.savePreferences(pPrefs);
		}

		@Override
		public void priceCollectorTradeEvent(PriceCollectorTradeBdx pBdx) {
		}
		
		void addTrade(StrategyServer_OwnTrade pTrade) {
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
		
//	public static String getParameterName(NameValuePair pPair) {
//		for (STRATEGY_STATUS tStatus : STRATEGY_STATUS.values()) {
//			if (tStatus.name().equals(pPair.getName())) {
//				return tStatus.getLabel();
//			}
//		}
//		return pPair.getName();
//	}
//	
//	public static String getParameterValue(NameValuePair pPair) {
//		if (pPair.getType() != null) {
//			for (STRATEGY_TYPE tType : STRATEGY_TYPE.values()) {
//				if (tType.name().equals(pPair.getType())) {
//					switch (tType) {
//						case TIME_MILLIS:
//							try {
//								Long tTime = new Long(pPair.getValue());
//								if (tTime == 0L) {
//									return "";
//								}
//								return AtcFormatters.getInstance().getTimeFormatter().format(tTime);
//							} catch (NumberFormatException e) {
//								return pPair.getValue();
//							}
//							
//						default:
//							return pPair.getValue();
//					}
//				}
//			}
//		}
//		return pPair.getValue();
//	}
//	
	
	class PositionMenuItem extends JMenuItem {
		PositionData mPositionData;
		
		PositionMenuItem() {
			super();
		}
		
		void setPositionData(PositionData pData) { 
			mPositionData = pData;
			setText(pData.getStrategyId() + " " + pData.getPLUnitId());
		}
	}
	
	class OurRenderer extends AlphaTableRenderer {
		private static final long serialVersionUID = 1L;
		
		public OurRenderer() {
			super();
		}
		
		@Override
		public void setColumnData(StaticColumnData[] pColumnData) {
			super.setColumnData(pColumnData);
		}
		
		@Override
		public Color getTableForegroundColor(int pRow) {
			return super.getTableForegroundColor(pRow);
		}

		@Override
		public Color getTableBackgroundColor(int pRow) {
// TODO GL This works except for column 0
//			if (pRow >= 0) {
//				PositionData tData = mDisplayList.get(mTable.convertRowIndexToModel(pRow));
//				double pl = tData.getTotalPL();
//				if (pl == 0d) {
//					return Color.white;
//				} else {
//					if (pl > 0d) {
//						return new Color(140, 209, 216);
//					} else if (pl < 0d) {
//						return new Color(207, 149, 161);
//					} else if (tData.getNetPosition() != 0) {
//						return Color.yellow;
//					}
//				}
//			}
			return super.getTableBackgroundColor(pRow);
		}

	}
	
	class BooleanEditor extends AbstractCellEditor implements TableCellEditor {
		private static final long serialVersionUID = 1L;

		final JCheckBox mCheckBox;
		public BooleanEditor(JCheckBox pBox) {
			mCheckBox = pBox;
			mCheckBox.setBackground(Color.green);
			mCheckBox.setSelected(false);
		}
		
		@Override
		public Component getTableCellEditorComponent(JTable table,
                Object value,
                boolean isSelected,
                int row,
                int column) 
		{
			return mCheckBox;
		}

		@Override
		public Object getCellEditorValue() {
			return mCheckBox.isSelected();
		}
		
	}

}

