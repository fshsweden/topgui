package com.fsh.topgui.models;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import com.fsh.topgui.framework.BaseFrame;

public class StrategyFrame extends BaseFrame {

	private StrategyDetailTableModel mDetailedModel;
	private JPanel mMainPanel;
	private JTextField mTimeTextField;
	private JPanel mCmdPanel;
	private PositionTableModel mPositionModel;
	
	// StrategyServerConnection mConnection, Window pOwner, PositionModel mPositionModel
	
	public StrategyFrame(PositionTableModel model) {
		super();
	
		// ??????????????????
		// new PositionTableModel(alpha.getStrategyServerConnection(), "some-id", /* owner */null);
		mPositionModel = model;
		
		// Start thread to display time with seconds
		// new UpdateTimeField(SwingUtilities.getWindowAncestor(ATCMain.getInstance())).start();
		
		
		// Create popup menus
//		final StrategyPopupMenu tStrategyMenu = new StrategyPopupMenu();
//		final JMenuItem tStrategyName = new JMenuItem();
//		tStrategyName.setEnabled(false);
//		tStrategyMenu.add(tStrategyName);
//		tStrategyMenu.addSeparator();
	}

	@Override
	protected JPanel createContentPane() {
		
		mMainPanel = new JPanel(new BorderLayout());
		//mMainPanel.setBackground(ATCMain.getInstance().getGuiEnvironment().getTableBackgroundColor());
		
		// Map in the detailed strategy model
		mDetailedModel = new StrategyDetailTableModel(alphaSystem.getStrategyServerConnection(), "StrategyDetailedModel", null /*, mPositionModel*/);
		mMainPanel.add(new JScrollPane(mDetailedModel.getComponent(null)), BorderLayout.CENTER);
		
		// Create cmd panels including clock
		JPanel tSouth = new JPanel(new BorderLayout());
		mMainPanel.add(tSouth, BorderLayout.PAGE_END);
		
		JPanel mCmdPanel = new JPanel(new FlowLayout());
		tSouth.add(mCmdPanel, BorderLayout.LINE_START);
		
		// Load and Terminate commands
		JButton tLoadAllButton = new JButton("Load All");
		tLoadAllButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mDetailedModel.loadAllStrategies();
			}
		});
		mCmdPanel.add(tLoadAllButton);
		
		JButton tTerminateAllButton = new JButton("Unload All");
		tTerminateAllButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mDetailedModel.unloadAllStrategies();	
			}
		});
		mCmdPanel.add(tTerminateAllButton);
		
		// Timer
		mTimeTextField = new JTextField();
		mTimeTextField.setText("00:00:00");
		mTimeTextField.setFont(new java.awt.Font("Tahoma",0,14));
		mTimeTextField.setEditable(false);
		mTimeTextField.setForeground(new java.awt.Color(0,56,113));
		mTimeTextField.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
		JPanel tTimePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		tTimePanel.add(mTimeTextField);
		tSouth.add(tTimePanel, BorderLayout.LINE_END);
		
		return mMainPanel;
	}

	
}
