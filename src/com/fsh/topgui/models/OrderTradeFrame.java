package com.fsh.topgui.models;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import com.ev112.codeblack.workbench.gui.tools.StaticColumnData;
import com.fsh.topgui.framework.BaseFrame;
import com.fsh.topgui.models.OwnOrderTradesTableModel.OrderRowData;

public class OrderTradeFrame extends BaseFrame {

	// Table
	private JTable mTable;
	private OwnOrderTradesTableModel ownOrderTradesModel;
	
	/**
	 * Helper class for when switch boxes change
	 */
	class CheckBoxAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
//			mRows.clear();
//			for (StatusEvent tEvent : mEvents) {
//				if (includeEvent(tEvent)) {
//					addRow(tEvent);
//				}
//			}
//			fireTableDataChanged();
		}		
	}

	class SwitchActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			ownOrderTradesModel.updateSelectedRows();
		}
	}
	
	
	// Display switches
	private final JCheckBox mActiveBox = new JCheckBox("Active only", false);
	private final JCheckBox mReverseTimeBox = new JCheckBox("Latest first", true);
	private final JCheckBox mOrderBox = new JCheckBox("Orders", true);
	private final JCheckBox mTradeBox = new JCheckBox("Trades", true);
	private final JCheckBox mSoundBox = new JCheckBox("Sound", false);
	
	
	public OrderTradeFrame(OwnOrderTradesTableModel model) {
		super();
		ownOrderTradesModel = model; // Too Late!!!
		
		setTitle("Trade Window");
		// getContentPane().setLayout(new MigLayout("", "[]", "[]"));
		
		super.setup();
		
		// Default sound to on if in production
		mSoundBox.setSelected(false);
	}

	@Override
	public String getWindowType() {
		return "OrderTrade";
	}

	@Override
	protected JPanel createContentPane() {
		
		JPanel tPanel = new JPanel(new BorderLayout());
		
		mTable = new JTable(ownOrderTradesModel) {
			private static final long serialVersionUID = 1L;
			@Override
			public String getToolTipText(MouseEvent e) {
				return ownOrderTradesModel.getRowDataForRow(convertRowIndexToModel(rowAtPoint(e.getPoint()))).getTooltipText();
			}
		};
		
		tPanel.add(new JScrollPane(mTable), BorderLayout.CENTER);
		mTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mTable.setGridColor(Color.BLACK);
		mTable.setAutoCreateRowSorter(true);
		mTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// Set table attributes
		StaticColumnData.setColumnAttributes(ownOrderTradesModel.mColumnData, mTable, ownOrderTradesModel.mId, ownOrderTradesModel.mResultRenderer, null);
		ownOrderTradesModel.mResultRenderer.setColumnAttributes(mTable.getColumnModel(), ownOrderTradesModel.mColumnData);
		
		mTable.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
		mTable.getTableHeader().setDefaultRenderer(ownOrderTradesModel.mResultRenderer);
		

		// Popups
		
		/*
		   final OrderPopupMenu tPopupMenu = new OrderPopupMenu();
		
		final JMenuItem tOrderInfoItem = new JMenuItem();
		tPopupMenu.add(tOrderInfoItem);
		tOrderInfoItem.setEnabled(false);
		
		final JMenuItem tOrderIdItem = new JMenuItem();
		tPopupMenu.add(tOrderIdItem);
		tOrderIdItem.setEnabled(false);
		tPopupMenu.addSeparator();
		JMenuItem tCancelItem = new JMenuItem("Cancel");
		tPopupMenu.add(tCancelItem);
		tCancelItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ManualTestCancelOrderReq tReq = new ManualTestCancelOrderReq();
				tReq.setOrderId(tPopupMenu.getOrderRowData().getId());
				tReq.setOwnReference(tPopupMenu.getOrderRowData().getOwnReference());

				ATCMain.getInstance().getServerModel().sendTestOrderMessage(tReq);
				
			}
		});
		*/
		
		mTable.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3 || e.getClickCount() == 2) {
					int tRow = mTable.convertRowIndexToModel(mTable.rowAtPoint(e.getPoint()));
					if (tRow >= 0 && ownOrderTradesModel.mRows.get(tRow) instanceof OrderRowData) {
						OrderRowData tOrderData = (OrderRowData) ownOrderTradesModel.mRows.get(tRow);
						
						/*
						tPopupMenu.setOrderRowData(tOrderData);
						tOrderInfoItem.setText(tOrderData.getTooltipText());
						tOrderIdItem.setText("Id " + tOrderData.getId());
						tPopupMenu.show(e.getComponent(), e.getX(), e.getY());
						*/
					}
				}
			}
		});

		
		// Do command line at bottom
		JPanel tCmdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tPanel.add(tCmdPanel, BorderLayout.PAGE_END);
		
		tCmdPanel.add(mActiveBox);
		tCmdPanel.add(mReverseTimeBox);
		tCmdPanel.add(mOrderBox);
		tCmdPanel.add(mTradeBox);
		tCmdPanel.add(mSoundBox);
		
		SwitchActionListener tSwitchListener = new SwitchActionListener();
		mActiveBox.addActionListener(tSwitchListener);
		mReverseTimeBox.addActionListener(tSwitchListener);
		mOrderBox.addActionListener(tSwitchListener);
		mTradeBox.addActionListener(tSwitchListener);
		
		return tPanel;
		
		
		
		
		
		
		
		
		
		
		
//						// Checkboxes
//						JCheckBox mReverseTimeBox = new JCheckBox("Reverse time", true);
//						JCheckBox mInfoBox = new JCheckBox("Information", true);
//						JCheckBox mErrorBox = new JCheckBox("Error", true);
//						JCheckBox mPriceBox = new JCheckBox("Price Collector", true);
//						JCheckBox mStrategyBox = new JCheckBox("Strategy Server", true);
//						JCheckBox mRiskBox = new JCheckBox("Risk Controller", true);
//						
//						CheckBoxAction mCheckBoxAction = new CheckBoxAction();
//						
//						
//						JPanel tPanel = new JPanel(new BorderLayout());
//						final JTable tTable = new JTable(new DefaultTableModel()) {
//							private static final long serialVersionUID = 1L;
//							@Override
//							public String getToolTipText(MouseEvent e) {
//								int tRowAtPoint = rowAtPoint(e.getPoint());
//								int tModelRow = convertRowIndexToModel(tRowAtPoint);
//								String tText = "Sample text"; // getRowDataForRow(tModelRow).getTooltipText();
//								return tText;
//							}
//						};
//						tPanel.add(new JScrollPane(tTable), BorderLayout.CENTER);
//						tTable.setAutoCreateRowSorter(true);
//						tTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//						tTable.setGridColor(Color.BLACK);
//						tTable.addMouseListener(new MouseAdapter() {
//							
//							@Override
//							public void mouseClicked(MouseEvent e) {
//								if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
//									int tRow = tTable.convertRowIndexToModel(tTable.rowAtPoint(e.getPoint()));
//									if (tRow >= 0) {
//				//						System.out.println("Selected row " + tRow + " object ");
//									}
//								}
//							}
//						});
//				
//						// Set table attributes
//				//		setColumnAttributes(tTable.getColumnModel());
//						
//						// Add command controls
//						JPanel tCmdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//						tPanel.add(tCmdPanel, BorderLayout.PAGE_END);
//						
//						tCmdPanel.add(mReverseTimeBox);
//						mReverseTimeBox.addActionListener(mCheckBoxAction);
//						
//						tCmdPanel.add(mInfoBox);
//						mInfoBox.addActionListener(mCheckBoxAction);
//				
//						tCmdPanel.add(mErrorBox);
//						mErrorBox.addActionListener(mCheckBoxAction);
//				
//						tCmdPanel.add(mPriceBox);
//						mPriceBox.addActionListener(mCheckBoxAction);
//				
//						tCmdPanel.add(mStrategyBox);
//						mStrategyBox.addActionListener(mCheckBoxAction);
//				
//						tCmdPanel.add(mRiskBox);
//						mRiskBox.addActionListener(mCheckBoxAction);
//						
//						return tPanel;
	}

}
