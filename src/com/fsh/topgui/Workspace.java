package com.fsh.topgui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.ini4j.Wini;

public class Workspace {

	private Map<JFrame, WorkspaceItem> items = new HashMap<JFrame, WorkspaceItem>();
	private Wini ini;

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
	
	public static void main(String[] args) {
		Workspace w = new Workspace();
		w.saveWorkspace();
		w.loadWorkspace();
	}
}
