package com.fsh.topgui.framework;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.ev112.codeblack.workbench.gui.tools.StaticColumnData;

/** Copyright EV112 Development AB, Stockholm, Sweden 2010 */

public class AlphaTableRenderer extends DefaultTableCellRenderer {

	// To remove warnings
	private static final long serialVersionUID = 1L;
	private ColorUtils colorUtils = new ColorUtils();
	
	// Column Data
	private StaticColumnData[] mColumnData = null;
	
	// Table colors
	private Color mTableBackgroundColor = null;
	private Color mTableForegroundColor = null;
	private Color mTableHeaderBackgroundColor = null;
	private Color mTableHeaderForegroundColor = null;
	
	// Table grid font
	private Font mTableContentFont = null;
	private Font mTableHeaderFont = null;
	
	// Default justify
	private int mDefaultJustify = JLabel.LEFT;
	
	// Constructor
	public AlphaTableRenderer() {
	}
	
	public AlphaTableRenderer(StaticColumnData[] pColumnData) {
		setColumnData(pColumnData);
	}
	
	public void setColumnData(StaticColumnData[] pColumnData) {
		mColumnData = pColumnData;

		mTableBackgroundColor = colorUtils.getColorByName("Azure");
		mTableForegroundColor = colorUtils.getColorByName("Black");
		mTableHeaderBackgroundColor = colorUtils.getColorByName("Brown");
		mTableHeaderForegroundColor = colorUtils.getColorByName("White");
		
		mTableContentFont = new Font(Font.MONOSPACED, Font.BOLD, 12);
		mTableHeaderFont = new Font(Font.MONOSPACED, Font.PLAIN, 12);		
	}
	
	public Color getTableBackgroundColor(int pRow) {
		return mTableBackgroundColor;
	}
	
	public Color getTableForegroundColor(int pRow) {
		return mTableForegroundColor;
	}
	
	/**
	 * Set default justify
	 */
	public void setDefaultJustify(int pJustify) {
		mDefaultJustify = pJustify;
	}
	
	/**
	 * Return the component
	 */
	@Override
	public Component getTableCellRendererComponent(
            JTable pJTable, Object pValue,
            boolean pIsSelected, boolean pHasFocus,
            int pRow, int pCol) {
		
		if (pRow < 0) {
			// Header row
			setBackground(mTableHeaderBackgroundColor);
			setForeground(mTableHeaderForegroundColor);
			setFont(mTableHeaderFont);
			
		} else {
			// Content row
			setBackground(getTableBackgroundColor(pRow));
			setForeground(getTableForegroundColor(pRow));
			setFont(mTableContentFont);
		}
		
		setText(pValue != null ? pValue.toString() : "");
		
		setHorizontalAlignment(mColumnData != null ? mColumnData[pCol].mJustify : mDefaultJustify);
		
		return this;
	}

	/**
	 * Utility method, set column attributes
	 */
	public void setColumnAttributes(TableColumnModel pColumnModel, StaticColumnData [] pColumnData) {
		for (int i = 0; i < pColumnData.length; i++) {
			TableColumn tColumn = pColumnModel.getColumn(i);
			if (pColumnData[i].mHeader != null) {
				tColumn.setHeaderValue(pColumnData[i].mHeader);
			}
			if (pColumnData[i].mWidth > 0) {
				tColumn.setPreferredWidth(pColumnData[i].mWidth);
				tColumn.setMinWidth(pColumnData[i].mWidth);
			}
			if (pColumnData[i].mRenderer != null) {
				tColumn.setCellRenderer(pColumnData[i].mRenderer);
				tColumn.setHeaderRenderer(pColumnData[i].mRenderer);
			}
			if (pColumnData[i].mEditor != null) {
				tColumn.setCellEditor(pColumnData[i].mEditor);
			}
		}
	}
	
}
