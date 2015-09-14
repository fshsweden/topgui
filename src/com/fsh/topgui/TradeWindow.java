package com.fsh.topgui;
import net.miginfocom.swing.MigLayout;

public class TradeWindow extends BaseWindow {

	public TradeWindow() {
		super();
		setTitle("Trade Window");
		getContentPane().setLayout(new MigLayout("", "[]", "[]"));
	}

	@Override
	public String getWindowType() {
		return "Trade";
	}

}
