package com.fsh.topgui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.ini4j.Wini;

import com.ev112.codeblack.simpleclient.alphasystem.AlphaSystem;

public class Workspace implements WindowListener {

	private static Integer last_x = 0;
	private static Integer last_y = 0;
	
	private Map<JFrame, WorkspaceItem> items = new HashMap<JFrame, WorkspaceItem>();
	private Wini ini;
	private AlphaSystem alphaSystem;

	public enum WindowType { };

	public Workspace(AlphaSystem alpha) {
		this.alphaSystem = alpha;
	}
	/*
	 * Places new windows where there is free space
	 */
	public class Coordinator {
		
		Dimension screenDim;
		public Coordinator() {
			screenDim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		}
		
		public Coordinate getCoordinateForNewWindow() {
			
			Integer x = getHighestAvailableX();
			Integer y = 100;
			if (x > screenDim.getWidth()) {
				x = 0;
				y = getHighestAvailableY();
			}
			return new Coordinate(x,y);
		}
	}
	private Coordinator coordinator = new Coordinator();
	
	
	public List<JFrame> getAllFrames() {
		List<JFrame> list = new ArrayList<JFrame>(items.keySet());
		return list;
	}
	
	private Integer getHighestAvailableX() {
		Integer min_x = Integer.MIN_VALUE;
		for (WorkspaceItem wsi : items.values()) {
			Integer avail_x = wsi.getWindowPositionX() + wsi.getWindowWidth(); 
			if (avail_x > min_x) {
				min_x = avail_x;
			}
		}
		return min_x;
	}
	
	private Integer getHighestAvailableY() {
		Integer min_y = Integer.MIN_VALUE;
		for (WorkspaceItem wsi : items.values()) {
			Integer avail_y = wsi.getWindowPositionY() + wsi.getWindowHeight();
			if (avail_y > min_y) {
				min_y = avail_y;
			}
		}
		return min_y;
	}
	
	
	public void addWorkspaceItem(WorkspaceItem item) {
		items.put(item.getFrame(), item);
	}
	
	public WorkspaceItem getWorkspaceItem(JFrame frame) {
		return items.get(frame);
	}
	
	public void removeWorkspaceItem(WorkspaceItem item) {
		items.remove(item.getFrame());
	}
	
	public void clear() {
		items.clear();
	}

	public List<WindowInfo> loadWorkspace() {
		List<WindowInfo> res = new ArrayList<WindowInfo>();
		
		try {
			ini = new Wini(new File("workspace.ini"));
			
			int num_windows = ini.get("app", "num_windows", int.class);
			System.out.println("Num windows is:" + num_windows);
			
			for (Integer count = 0; count < num_windows; count++) {
				String mainSectionId = "win"+count.toString();
				
				
				Integer x		= ini.get(mainSectionId,"X",Integer.class);
				Integer y		= ini.get(mainSectionId,"Y",Integer.class);
				Integer width	= ini.get(mainSectionId,"Width",Integer.class);
				Integer height	= ini.get(mainSectionId,"Height",Integer.class);
				String windowType = ini.get(mainSectionId,"WindowType");

				WindowInfo wi = new WindowInfo();
				wi.put("X",x.toString());
				wi.put("Y",y.toString());
				wi.put("Width",width.toString());
				wi.put("Height",height.toString());
				wi.put("WindowType",windowType.toString());
				
				res.add(wi);
			}
		} catch (IOException e) {
			return null;
		}
		
		return res;
	}

	public void saveWorkspace() {
		
		try {
			File inioutfile = new File("workspace.ini");
			//Insert here.
			if(!inioutfile.exists()) {
			    if(!inioutfile.createNewFile()) return;
			}
			ini = new Wini(new File(inioutfile.getAbsolutePath()));			
			
			// Clear slate!
			ini.clear();
			
			System.out.println("Saving " + items.size() + " workspace items");
	        ini.put("app", "num_windows", items.size());
			
	        Integer count = 0;
	        
			for (WorkspaceItem w : items.values()) {
				String mainSectionId = "win"+count.toString();
				ini.put(mainSectionId, "Dlg", "value");
				ini.put(mainSectionId, "X", w.getWindowPositionX());	// WorkspaceItem has a pointer to the JFrame!
				ini.put(mainSectionId, "Y", w.getWindowPositionY());	// WorkspaceItem has a pointer to the JFrame!
				ini.put(mainSectionId, "Height", w.getWindowHeight());	// WorkspaceItem has a pointer to the JFrame!
				ini.put(mainSectionId, "Width", w.getWindowWidth());	// WorkspaceItem has a pointer to the JFrame!
				ini.put(mainSectionId, "WindowType", w.getWindowType());	
				count++;
			}
			
	        ini.store();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createNewPositionWindow() {
		Coordinate c = coordinator.getCoordinateForNewWindow();
		PositionWindow pd = new PositionWindow(alphaSystem);
		pd.setLocation(c.getX(),c.getY());
		pd.addWindowListener(this);
		pd.setVisible(true);
		addWorkspaceItem(pd);
	}

	public void createNewOrderWindow() {
		Coordinate c = coordinator.getCoordinateForNewWindow();
		OrderWindow ow = new OrderWindow(alphaSystem);
		ow.setLocation(c.getX(),c.getY());
		ow.addWindowListener(this);
		ow.setVisible(true);
		addWorkspaceItem(ow);
	}

	public void createNewTradeWindow() {
		Coordinate c = coordinator.getCoordinateForNewWindow();
		TradeWindow tw = new TradeWindow(alphaSystem);
		tw.setLocation(c.getX(),c.getY());
		tw.addWindowListener(this);
		tw.setVisible(true);
		addWorkspaceItem(tw);
	}

	public void createNewTestWindow() {
		Coordinate c = coordinator.getCoordinateForNewWindow();
		TestWindow tw = new TestWindow(alphaSystem);
		tw.setLocation(c.getX(),c.getY());
		tw.addWindowListener(this);
		tw.setVisible(true);
		addWorkspaceItem(tw);
	}

	public void createNewType2Window() {
		Coordinate c = coordinator.getCoordinateForNewWindow();
		ServerStatusFrame tw = new ServerStatusFrame(alphaSystem);
		tw.setLocation(c.getX(),c.getY());
		tw.addWindowListener(this);
		tw.setVisible(true);
		addWorkspaceItem(tw);
	}
	
	public void loadWorkspaceAndInstantiateWindows() {
		clear();
		
		List<WindowInfo> w = loadWorkspace();
		for (WindowInfo wi : w) {
			switch (wi.get("WindowType")) {
				case "Test":
				{
					Coordinate c = coordinator.getCoordinateForNewWindow();
					TestWindow pd = new TestWindow(alphaSystem);
					pd.addWindowListener(this);
					pd.setVisible(true);
					
					pd.setLocation(wi.getInt("X"), wi.getInt("Y"));
					pd.setSize(wi.getInt("Width"), wi.getInt("Height"));
					
					addWorkspaceItem(pd);
				}
				break;
				case "Type2":
				{
					Coordinate c = coordinator.getCoordinateForNewWindow();
					ServerStatusFrame pd = new ServerStatusFrame(alphaSystem);
					pd.addWindowListener(this);
					pd.setVisible(true);
					
					pd.setLocation(wi.getInt("X"), wi.getInt("Y"));
					pd.setSize(wi.getInt("Width"), wi.getInt("Height"));
					
					addWorkspaceItem(pd);
				}
				break;
				case "Trade":
				{
					Coordinate c = coordinator.getCoordinateForNewWindow();
					TradeWindow pd = new TradeWindow(alphaSystem);
					pd.addWindowListener(this);
					pd.setVisible(true);
					
					pd.setLocation(wi.getInt("X"), wi.getInt("Y"));
					pd.setSize(wi.getInt("Width"), wi.getInt("Height"));
					
					addWorkspaceItem(pd);
				}
				break;
				case "Order":
				{
					Coordinate c = coordinator.getCoordinateForNewWindow();
					OrderWindow ow = new OrderWindow(alphaSystem);
					ow.addWindowListener(this);
					ow.setVisible(true);

					ow.setLocation(wi.getInt("X"), wi.getInt("Y"));
					ow.setSize(wi.getInt("Width"), wi.getInt("Height"));
					
					addWorkspaceItem(ow);
				}
				break;
				case "Position":
				{
					Coordinate c = coordinator.getCoordinateForNewWindow();
					PositionWindow pd = new PositionWindow(alphaSystem);
					pd.addWindowListener(this);
					pd.setVisible(true);

					pd.setLocation(wi.getInt("X"), wi.getInt("Y"));
					pd.setSize(wi.getInt("Width"), wi.getInt("Height"));
					
					addWorkspaceItem(pd);
				}
				break;
			}
		}
	}


	@Override
	public void windowOpened(WindowEvent e) {
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println("Closing! " + e.toString());
		if (e.getWindow() instanceof BaseFrame) {
			BaseFrame bw = (BaseFrame)e.getWindow();
			removeWorkspaceItem(bw);
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {
		System.out.println("Closed! " + e.toString());
	}

	@Override
	public void windowIconified(WindowEvent e) {
		System.out.println("Minimized! " + e.toString());
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		System.out.println("Restored! " + e.toString());
	}

	@Override
	public void windowActivated(WindowEvent e) {
		System.out.println("Activated! " + e.toString());
		
		Point loc = e.getWindow().getLocation();
		Dimension dim = e.getWindow().getSize();
		
		JFrame frame = (JFrame)e.getWindow();
		WorkspaceItem wi = getWorkspaceItem(frame);
		if (frame != null) {
			
		}
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		System.out.println("Deactivated! " + e.toString());
	}
	
	
	
	public static void main(String[] args) {
//		Workspace w = new Workspace();
//		w.saveWorkspace();
//		w.loadWorkspace();
	}

}
