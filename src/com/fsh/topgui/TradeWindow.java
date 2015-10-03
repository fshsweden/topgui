package com.fsh.topgui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.ev112.codeblack.simpleclient.alphasystem.AlphaSystem;

import net.miginfocom.swing.MigLayout;

public class TradeWindow extends BaseFrame {

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
	
	
	
	
	public TradeWindow(AlphaSystem alpha) {
		super(alpha);
		setTitle("Trade Window");
		getContentPane().setLayout(new MigLayout("", "[]", "[]"));
	}

	@Override
	public String getWindowType() {
		return "Trade";
	}

	@Override
	protected JPanel createContentPane() {
		
		// Checkboxes
		JCheckBox mReverseTimeBox = new JCheckBox("Reverse time", true);
		JCheckBox mInfoBox = new JCheckBox("Information", true);
		JCheckBox mErrorBox = new JCheckBox("Error", true);
		JCheckBox mPriceBox = new JCheckBox("Price Collector", true);
		JCheckBox mStrategyBox = new JCheckBox("Strategy Server", true);
		JCheckBox mRiskBox = new JCheckBox("Risk Controller", true);
		
		CheckBoxAction mCheckBoxAction = new CheckBoxAction();
		
		
		JPanel tPanel = new JPanel(new BorderLayout());
		final JTable tTable = new JTable(new DefaultTableModel()) {
			private static final long serialVersionUID = 1L;
			@Override
			public String getToolTipText(MouseEvent e) {
				int tRowAtPoint = rowAtPoint(e.getPoint());
				int tModelRow = convertRowIndexToModel(tRowAtPoint);
				String tText = "Sample text"; // getRowDataForRow(tModelRow).getTooltipText();
				return tText;
			}
		};
		tPanel.add(new JScrollPane(tTable), BorderLayout.CENTER);
		tTable.setAutoCreateRowSorter(true);
		tTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tTable.setGridColor(Color.BLACK);
		tTable.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
					int tRow = tTable.convertRowIndexToModel(tTable.rowAtPoint(e.getPoint()));
					if (tRow >= 0) {
//						System.out.println("Selected row " + tRow + " object ");
					}
				}
			}
		});

		// Set table attributes
//		setColumnAttributes(tTable.getColumnModel());
		
		// Add command controls
		JPanel tCmdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tPanel.add(tCmdPanel, BorderLayout.PAGE_END);
		
		tCmdPanel.add(mReverseTimeBox);
		mReverseTimeBox.addActionListener(mCheckBoxAction);
		
		tCmdPanel.add(mInfoBox);
		mInfoBox.addActionListener(mCheckBoxAction);

		tCmdPanel.add(mErrorBox);
		mErrorBox.addActionListener(mCheckBoxAction);

		tCmdPanel.add(mPriceBox);
		mPriceBox.addActionListener(mCheckBoxAction);

		tCmdPanel.add(mStrategyBox);
		mStrategyBox.addActionListener(mCheckBoxAction);

		tCmdPanel.add(mRiskBox);
		mRiskBox.addActionListener(mCheckBoxAction);
		
		return tPanel;
	}

}
