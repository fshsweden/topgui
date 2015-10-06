package com.fsh.topgui.slask;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TestPanel2 extends JPanel {
	private JLabel lblId;
	private JTextField textParamIntervalInSec;
	JTextField textMaxChannelPercentSpread;
	JTextField textMinChannelPercentSpread;
	JTextField textMaxPriceChange;
	JTextField textMinPriceChange;
	JTextField textMinTimeBetweenNewChannelsInSec;
	JTextField textMinTradesInChannel;
	JTextField textChannelSafetyPercentage;
	JLabel lblConfTrades;
	JTextField textNumConfTrades;
	JSeparator separator;
	JLabel lblComment;
	JTextField textComment;
	JLabel lblSelectParameterSet;
	JComboBox cbParams;
	JButton btnSave;
	JButton btnDelete;
	JLabel lblDivide_1;
	private JPanel jpl;
	
	/**
	 * Create the panel.
	 */
	public TestPanel2() {
		
		GridBagLayout gbl_jpl = new GridBagLayout();
		gbl_jpl.columnWidths = new int[]{17, 0, 0, 0, 0, -14};
		gbl_jpl.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0};
		
		jpl = new JPanel();
		jpl.setLayout(gbl_jpl);
		{
			lblId = new JLabel("");
			GridBagConstraints gbc_lblId = new GridBagConstraints();
			gbc_lblId.anchor = GridBagConstraints.SOUTH;
			gbc_lblId.insets = new Insets(0, 0, 5, 0);
			gbc_lblId.gridx = 5;
			gbc_lblId.gridy = 0;
			jpl.add(lblId, gbc_lblId);
		}

		{
			JLabel lblParamIntervalInSec = new JLabel("Interval(s):");
			GridBagConstraints gbc_lblParamIntervalInSec = new GridBagConstraints();
			gbc_lblParamIntervalInSec.anchor = GridBagConstraints.SOUTH;
			gbc_lblParamIntervalInSec.insets = new Insets(0, 0, 5, 5);
			gbc_lblParamIntervalInSec.gridx = 1;
			gbc_lblParamIntervalInSec.gridy = 1;
			jpl.add(lblParamIntervalInSec, gbc_lblParamIntervalInSec);
		}
		{
			textParamIntervalInSec = new JTextField();
			GridBagConstraints gbc_textParamIntervalInSec = new GridBagConstraints();
			gbc_textParamIntervalInSec.fill = GridBagConstraints.HORIZONTAL;
			gbc_textParamIntervalInSec.anchor = GridBagConstraints.SOUTH;
			gbc_textParamIntervalInSec.insets = new Insets(0, 0, 5, 5);
			gbc_textParamIntervalInSec.gridx = 3;
			gbc_textParamIntervalInSec.gridy = 1;
			jpl.add(textParamIntervalInSec, gbc_textParamIntervalInSec);
			textParamIntervalInSec.setColumns(4);
		}
		{
			JLabel lblMaxChannelPercentSpread = new JLabel("(*) Max Spread %");
			GridBagConstraints gbc_lblMaxChannelPercentSpread = new GridBagConstraints();
			gbc_lblMaxChannelPercentSpread.insets = new Insets(0, 0, 5, 5);
			gbc_lblMaxChannelPercentSpread.anchor = GridBagConstraints.SOUTH;
			gbc_lblMaxChannelPercentSpread.gridx = 1;
			gbc_lblMaxChannelPercentSpread.gridy = 2;
			jpl.add(lblMaxChannelPercentSpread, gbc_lblMaxChannelPercentSpread);
		}
		{
			textMaxChannelPercentSpread = new JTextField();
			GridBagConstraints gbc_textMaxChannelPercentSpread = new GridBagConstraints();
			gbc_textMaxChannelPercentSpread.fill = GridBagConstraints.HORIZONTAL;
			gbc_textMaxChannelPercentSpread.anchor = GridBagConstraints.SOUTH;
			gbc_textMaxChannelPercentSpread.insets = new Insets(0, 0, 5, 5);
			gbc_textMaxChannelPercentSpread.gridx = 3;
			gbc_textMaxChannelPercentSpread.gridy = 2;
			jpl.add(textMaxChannelPercentSpread, gbc_textMaxChannelPercentSpread);
			textMaxChannelPercentSpread.setColumns(4);
		}
		{
			JLabel lblMinChannelPercentSpread = new JLabel("(*) Min Spread %");
			GridBagConstraints gbc_lblMinChannelPercentSpread = new GridBagConstraints();
			gbc_lblMinChannelPercentSpread.anchor = GridBagConstraints.SOUTH;
			gbc_lblMinChannelPercentSpread.insets = new Insets(0, 0, 5, 5);
			gbc_lblMinChannelPercentSpread.gridx = 1;
			gbc_lblMinChannelPercentSpread.gridy = 3;
			jpl.add(lblMinChannelPercentSpread, gbc_lblMinChannelPercentSpread);
		}
		{
			textMinChannelPercentSpread = new JTextField();
			GridBagConstraints gbc_textMinChannelPercentSpread = new GridBagConstraints();
			gbc_textMinChannelPercentSpread.fill = GridBagConstraints.HORIZONTAL;
			gbc_textMinChannelPercentSpread.anchor = GridBagConstraints.SOUTH;
			gbc_textMinChannelPercentSpread.insets = new Insets(0, 0, 5, 5);
			gbc_textMinChannelPercentSpread.gridx = 3;
			gbc_textMinChannelPercentSpread.gridy = 3;
			jpl.add(textMinChannelPercentSpread, gbc_textMinChannelPercentSpread);
			textMinChannelPercentSpread.setColumns(4);
		}
		{
			JLabel lblMaxPriceChange = new JLabel("Max Price Chg %");
			GridBagConstraints gbc_lblMaxPriceChange = new GridBagConstraints();
			gbc_lblMaxPriceChange.anchor = GridBagConstraints.SOUTHWEST;
			gbc_lblMaxPriceChange.insets = new Insets(0, 0, 5, 5);
			gbc_lblMaxPriceChange.gridx = 1;
			gbc_lblMaxPriceChange.gridy = 4;
			jpl.add(lblMaxPriceChange, gbc_lblMaxPriceChange);
		}
		{
			textMaxPriceChange = new JTextField();
			GridBagConstraints gbc_textMaxPriceChange = new GridBagConstraints();
			gbc_textMaxPriceChange.fill = GridBagConstraints.HORIZONTAL;
			gbc_textMaxPriceChange.anchor = GridBagConstraints.SOUTH;
			gbc_textMaxPriceChange.insets = new Insets(0, 0, 5, 5);
			gbc_textMaxPriceChange.gridx = 3;
			gbc_textMaxPriceChange.gridy = 4;
			jpl.add(textMaxPriceChange, gbc_textMaxPriceChange);
			textMaxPriceChange.setColumns(4);
		}
		{
			JLabel lblMinpricechange = new JLabel("Min Price Chg %");
			GridBagConstraints gbc_lblMinpricechange = new GridBagConstraints();
			gbc_lblMinpricechange.anchor = GridBagConstraints.SOUTH;
			gbc_lblMinpricechange.insets = new Insets(0, 0, 5, 5);
			gbc_lblMinpricechange.gridx = 1;
			gbc_lblMinpricechange.gridy = 5;
			jpl.add(lblMinpricechange, gbc_lblMinpricechange);
		}
		{
			textMinPriceChange = new JTextField();
			GridBagConstraints gbc_textMinPriceChange = new GridBagConstraints();
			gbc_textMinPriceChange.fill = GridBagConstraints.HORIZONTAL;
			gbc_textMinPriceChange.anchor = GridBagConstraints.SOUTH;
			gbc_textMinPriceChange.insets = new Insets(0, 0, 5, 5);
			gbc_textMinPriceChange.gridx = 3;
			gbc_textMinPriceChange.gridy = 5;
			jpl.add(textMinPriceChange, gbc_textMinPriceChange);
			textMinPriceChange.setColumns(4);
		}
		{
			JLabel lblMintimebetweennewchannelsinsec = new JLabel("Min Time Btw Chn");
			GridBagConstraints gbc_lblMintimebetweennewchannelsinsec = new GridBagConstraints();
			gbc_lblMintimebetweennewchannelsinsec.anchor = GridBagConstraints.SOUTH;
			gbc_lblMintimebetweennewchannelsinsec.insets = new Insets(0, 0, 5, 5);
			gbc_lblMintimebetweennewchannelsinsec.gridx = 1;
			gbc_lblMintimebetweennewchannelsinsec.gridy = 6;
			jpl.add(lblMintimebetweennewchannelsinsec, gbc_lblMintimebetweennewchannelsinsec);
		}
		{
			textMinTimeBetweenNewChannelsInSec = new JTextField();
			GridBagConstraints gbc_textMinTimeBetweenNewChannelsInSec = new GridBagConstraints();
			gbc_textMinTimeBetweenNewChannelsInSec.fill = GridBagConstraints.HORIZONTAL;
			gbc_textMinTimeBetweenNewChannelsInSec.anchor = GridBagConstraints.SOUTH;
			gbc_textMinTimeBetweenNewChannelsInSec.insets = new Insets(0, 0, 5, 5);
			gbc_textMinTimeBetweenNewChannelsInSec.gridx = 3;
			gbc_textMinTimeBetweenNewChannelsInSec.gridy = 6;
			jpl.add(textMinTimeBetweenNewChannelsInSec, gbc_textMinTimeBetweenNewChannelsInSec);
			textMinTimeBetweenNewChannelsInSec.setColumns(4);
		}
		{
			JLabel lblMintradesinchannel = new JLabel("Min Trd In Chn");
			GridBagConstraints gbc_lblMintradesinchannel = new GridBagConstraints();
			gbc_lblMintradesinchannel.anchor = GridBagConstraints.SOUTH;
			gbc_lblMintradesinchannel.insets = new Insets(0, 0, 5, 5);
			gbc_lblMintradesinchannel.gridx = 1;
			gbc_lblMintradesinchannel.gridy = 7;
			jpl.add(lblMintradesinchannel, gbc_lblMintradesinchannel);
		}
		{
			textMinTradesInChannel = new JTextField();
			GridBagConstraints gbc_textMinTradesInChannel = new GridBagConstraints();
			gbc_textMinTradesInChannel.fill = GridBagConstraints.HORIZONTAL;
			gbc_textMinTradesInChannel.anchor = GridBagConstraints.SOUTH;
			gbc_textMinTradesInChannel.insets = new Insets(0, 0, 5, 5);
			gbc_textMinTradesInChannel.gridx = 3;
			gbc_textMinTradesInChannel.gridy = 7;
			jpl.add(textMinTradesInChannel, gbc_textMinTradesInChannel);
			textMinTradesInChannel.setColumns(4);
		}

		
		
		
		
		{
			JLabel lblChannelsafetypercentage = new JLabel("Safety %");
			GridBagConstraints gbc_lblChannelsafetypercentage = new GridBagConstraints();
			gbc_lblChannelsafetypercentage.anchor = GridBagConstraints.SOUTH;
			gbc_lblChannelsafetypercentage.insets = new Insets(0, 0, 5, 5);
			gbc_lblChannelsafetypercentage.gridx = 1;
			gbc_lblChannelsafetypercentage.gridy = 8;
			jpl.add(lblChannelsafetypercentage, gbc_lblChannelsafetypercentage);
		}
		{
			textChannelSafetyPercentage = new JTextField();
			GridBagConstraints gbc_textChannelSafetyPercentage = new GridBagConstraints();
			gbc_textChannelSafetyPercentage.fill = GridBagConstraints.HORIZONTAL;
			gbc_textChannelSafetyPercentage.anchor = GridBagConstraints.SOUTH;
			gbc_textChannelSafetyPercentage.insets = new Insets(0, 0, 5, 5);
			gbc_textChannelSafetyPercentage.gridx = 3;
			gbc_textChannelSafetyPercentage.gridy = 8;
			jpl.add(textChannelSafetyPercentage, gbc_textChannelSafetyPercentage);
			textChannelSafetyPercentage.setColumns(4);
		}
		{
			lblConfTrades = new JLabel("# Conf Trades");
			GridBagConstraints gbc_lblConfTrades = new GridBagConstraints();
			gbc_lblConfTrades.anchor = GridBagConstraints.SOUTH;
			gbc_lblConfTrades.insets = new Insets(0, 0, 5, 5);
			gbc_lblConfTrades.gridx = 1;
			gbc_lblConfTrades.gridy = 9;
			jpl.add(lblConfTrades, gbc_lblConfTrades);
		}
		{
			textNumConfTrades = new JTextField();
			GridBagConstraints gbc_textNumConfTrades = new GridBagConstraints();
			gbc_textNumConfTrades.fill = GridBagConstraints.HORIZONTAL;
			gbc_textNumConfTrades.anchor = GridBagConstraints.SOUTH;
			gbc_textNumConfTrades.insets = new Insets(0, 0, 5, 5);
			gbc_textNumConfTrades.gridx = 3;
			gbc_textNumConfTrades.gridy = 9;
			jpl.add(textNumConfTrades, gbc_textNumConfTrades);
			textNumConfTrades.setColumns(10);
		}
		{
			separator = new JSeparator();
			GridBagConstraints gbc_separator = new GridBagConstraints();
			gbc_separator.anchor = GridBagConstraints.SOUTH;
			gbc_separator.gridwidth = 5;
			gbc_separator.insets = new Insets(0, 0, 5, 0);
			gbc_separator.gridx = 1;
			gbc_separator.gridy = 10;
			jpl.add(separator, gbc_separator);
		}
		{
			lblComment = new JLabel("Comment");
			GridBagConstraints gbc_lblComment = new GridBagConstraints();
			gbc_lblComment.insets = new Insets(0, 0, 5, 5);
			gbc_lblComment.anchor = GridBagConstraints.SOUTH;
			gbc_lblComment.gridx = 1;
			gbc_lblComment.gridy = 11;
			jpl.add(lblComment, gbc_lblComment);
		}
		
		
		
		
		{
			lblSelectParameterSet = new JLabel("Select Param Set:");
			GridBagConstraints gbc_lblSelectParameterSet = new GridBagConstraints();
			gbc_lblSelectParameterSet.insets = new Insets(0, 0, 5, 5);
			gbc_lblSelectParameterSet.anchor = GridBagConstraints.SOUTH;
			gbc_lblSelectParameterSet.gridx = 1;
			gbc_lblSelectParameterSet.gridy = 13;
			jpl.add(lblSelectParameterSet, gbc_lblSelectParameterSet);
		}
		
		{
			cbParams = new JComboBox<>();
//			cbParams = new JComboBox<ChannelEngineParams>();
//			cbParams.setRenderer(new ChannelEngineParamsRenderer());
			
			GridBagConstraints gbc_comboBox = new GridBagConstraints();
			gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBox.anchor = GridBagConstraints.SOUTH;
			gbc_comboBox.insets = new Insets(0, 0, 5, 5);
			gbc_comboBox.gridx = 3;
			gbc_comboBox.gridy = 13;
			jpl.add(cbParams, gbc_comboBox);
			
			cbParams.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
				}
			});
			cbParams.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
				}
			});
		}
		
		
		
		{
			btnSave = new JButton("Save");
			GridBagConstraints gbc_btnSave = new GridBagConstraints();
			gbc_btnSave.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnSave.anchor = GridBagConstraints.SOUTH;
			gbc_btnSave.insets = new Insets(0, 0, 5, 5);
			gbc_btnSave.gridx = 3;
			gbc_btnSave.gridy = 14;
			jpl.add(btnSave, gbc_btnSave);
			btnSave.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
				}
			});
		}
		
		{
			btnDelete = new JButton("Delete");
			GridBagConstraints gbc_btnDelete = new GridBagConstraints();
			gbc_btnDelete.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnDelete.anchor = GridBagConstraints.SOUTH;
			gbc_btnDelete.insets = new Insets(0, 0, 5, 5);
			gbc_btnDelete.gridx = 3;
			gbc_btnDelete.gridy = 15;
			jpl.add(btnDelete, gbc_btnDelete);
			
			btnDelete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
				}
			});
		}
		{
			lblDivide_1 = new JLabel("(*) = Divide value with 100 before entering");
			GridBagConstraints gbc_lblDivide_1 = new GridBagConstraints();
			gbc_lblDivide_1.anchor = GridBagConstraints.SOUTH;
			gbc_lblDivide_1.gridwidth = 3;
			gbc_lblDivide_1.insets = new Insets(0, 0, 0, 5);
			gbc_lblDivide_1.gridx = 1;
			gbc_lblDivide_1.gridy = 16;
			jpl.add(lblDivide_1, gbc_lblDivide_1);
		}
		
		{
			textComment = new JTextField();
			GridBagConstraints gbc_textComment = new GridBagConstraints();
			gbc_textComment.fill = GridBagConstraints.HORIZONTAL;
			gbc_textComment.anchor = GridBagConstraints.SOUTH;
			gbc_textComment.insets = new Insets(0, 0, 5, 5);
			gbc_textComment.gridx = 3;
			gbc_textComment.gridy = 11;
			jpl.add(textComment, gbc_textComment);
			textComment.setColumns(4);
			
			textComment.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					if (textComment.getText().equals(""))
						btnSave.setEnabled(false);
					else
						btnSave.setEnabled(true);
				}
				@Override
				public void insertUpdate(DocumentEvent e) {
					if (textComment.getText().equals(""))
						btnSave.setEnabled(false);
					else
						btnSave.setEnabled(true);
				}
				@Override
				public void changedUpdate(DocumentEvent e) {
					if (textComment.getText().equals(""))
						btnSave.setEnabled(false);
					else
						btnSave.setEnabled(true);
				}
			});
		}			
	}
}
