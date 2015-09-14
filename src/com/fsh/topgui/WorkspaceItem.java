package com.fsh.topgui;

import javax.swing.JFrame;

public interface WorkspaceItem {

	public JFrame  getFrame();
	
	public Integer getWindowPositionX();
	public Integer getWindowPositionY();
	public Integer getWindowWidth();
	public Integer getWindowHeight();
	
	public String getWindowType();
	
	public void setWindowPosition(Integer x, Integer y);
	public void setWindowSize(Integer w, Integer h);
	
	public void setWindowType(String t);
}
