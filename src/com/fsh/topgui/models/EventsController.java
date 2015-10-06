package com.fsh.topgui.models;

import com.ev112.codeblack.simpleclient.alphasystem.AlphaSystem;
import com.fsh.topgui.framework.BaseFrame;

public class EventsController {
	EventsTableModel model;
	BaseFrame frame;
	
	public EventsController(AlphaSystem alpha) {
		model = new EventsTableModel(alpha);
		frame = new EventsFrame(model);
	}
}
