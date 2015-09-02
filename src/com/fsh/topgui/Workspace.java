package com.fsh.topgui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ini4j.Wini;

public class Workspace {

	private List<WorkspaceItem> items = new ArrayList<WorkspaceItem>();
	private Wini ini;

	public void addWorkspaceItem(WorkspaceItem item) {
		items.add(item);
	}
	
	public void clear() {
		items.clear();
	}
	
	public void removeWorkspaceItem(WorkspaceItem item) {
		items.remove(item);
	}

	public List<WindowInfo> loadWorkspace() {
		List<WindowInfo> res = new ArrayList<WindowInfo>();
		
		try {
			ini = new Wini(new File("workspace.ini"));
			
			int num_windows = ini.get("app", "num_windows", int.class);
			System.out.println("Num windows is:" + num_windows);
			
			for (Integer count = 0; count < num_windows; count++) {
				String mainSectionId = "win"+count.toString();
				
				
				Integer x		= ini.get(mainSectionId,"PositionX",Integer.class);
				Integer y		= ini.get(mainSectionId,"PositionY",Integer.class);
				Integer width	= ini.get(mainSectionId,"Width",Integer.class);
				Integer height	= ini.get(mainSectionId,"Height",Integer.class);
				String windowType = ini.get(mainSectionId,"WindowType");

				WindowInfo wi = new WindowInfo();
//				wi.put("X",x.toString());
//				wi.put("Y",y.toString());
//				wi.put("Width",width.toString());
//				wi.put("Height",height.toString());
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
	        
			for (WorkspaceItem w : items) {
				String mainSectionId = "win"+count.toString();
				ini.put(mainSectionId, "Dlg", "value");
				ini.put(mainSectionId, "X", w.getWindowPositionX());
				ini.put(mainSectionId, "Y", w.getWindowPositionY());
				ini.put(mainSectionId, "Height", w.getWindowHeight());
				ini.put(mainSectionId, "Width", w.getWindowWidth());
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
