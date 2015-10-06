package com.fsh.topgui.framework;

import java.util.HashMap;

public class WindowInfo extends HashMap<String,String> {

	public WindowInfo() {
		
	}

	public Integer getInt(final String key) {
		return Integer.parseInt(get(key));
	}
	
	public Double getDouble(final String key) {
		return Double.parseDouble(get(key));
	}
}
