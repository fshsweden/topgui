package com.fsh.topgui.models;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import com.ev112.codeblack.common.generated.messages.StatusEvent;
import com.ev112.codeblack.workbench.gui.tools.StaticColumnData;
import com.fsh.topgui.framework.BaseFrame;
import com.fsh.topgui.models.EventsTableModel.RowData;

public class EventsFrame extends BaseFrame {

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
	
	private final CheckBoxAction mCheckBoxAction = new CheckBoxAction();
	
	/**
	 * Helper class for when switch boxes change
	 */
	public class CheckBoxAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			eventsModel.reloadEvents();
		}		
	}
	
	private EventsTableModel eventsModel;
	
	
	// Last event -- to make sure we don't swamp the user
//	private long mLastEventTime = 0L;
//	private boolean mSwamped = false;
	
	public EventsFrame(EventsTableModel model) {
		super();
		eventsModel = model;
		setBounds(100, 100, 676, 565);
	}

	@Override
	protected JPanel createContentPane() {
		
		JPanel tPanel = new JPanel(new BorderLayout());
		
		final JTable tTable = new JTable(eventsModel) {
			private static final long serialVersionUID = 1L;
			@Override
			public String getToolTipText(MouseEvent e) {
				int tRowAtPoint = rowAtPoint(e.getPoint());
				int tModelRow = convertRowIndexToModel(tRowAtPoint);
				String tText = eventsModel.getRowDataForRow(tModelRow).getTooltipText();
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
		// This is WEIRD!!!!!!!!!
		
		eventsModel.setColumnAttributes(tTable.getColumnModel());
		
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
		
		
		
		
		
		
		
		
		
		
		
		
		
		
/*		
		JPanel panel = new JPanel(new BorderLayout());
		
		JPanel topPanel = new JPanel();
		topPanel.setBackground(Color.BLUE);
		
			topPanel.setLayout(new FlowLayout());
			
				JTextField search = new JTextField();
				search.setColumns(10);
				
			topPanel.add(search);
			topPanel.add(new JButton("START"));
			topPanel.add(new JButton("START"));
		
		
		JPanel middlePanel = new JPanel();
		middlePanel.setBackground(Color.GREEN);
		
//			middlePanel.setLayout(new MigLayout());
//			middlePanel.add(new JLabel("Enter size:"),   "");
//			middlePanel.add(new JTextField("",4),          "wrap");
//			middlePanel.add(new JLabel("Enter weight:"), "");
//			middlePanel.add(new JTextField("",8),          "");	
		
			
			JTable table = new JTable(new EventsModel());
			JScrollPane scrollPane = new JScrollPane(table);
			
			middlePanel.add(scrollPane);
		
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.BLACK);

			bottomPanel.setLayout(new FlowLayout());
			bottomPanel.add(new JButton("START"));
			bottomPanel.add(new JButton("START"));
			bottomPanel.add(new JButton("START"));
		
		
		
		panel.add(topPanel, BorderLayout.PAGE_START);
		panel.add(middlePanel, BorderLayout.CENTER);
		panel.add(bottomPanel, BorderLayout.PAGE_END);
		
		return panel;
*/
	}
		
}
