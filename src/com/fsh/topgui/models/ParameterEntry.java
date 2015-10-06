package com.fsh.topgui.models;

import com.ev112.codeblack.common.model.parameters.ParameterDictionary.ParameterIdentity;

/** Copyright EV112 Development AB, Stockholm, Sweden 2010 */

public class ParameterEntry implements Comparable<ParameterEntry> {

	private final String mName;
	private String mValue;
	private ParameterIdentity mIdentity;
	
	public ParameterEntry(String pName, String pValue) {
		mName = pName;
		mValue = pValue;
		
		for (ParameterIdentity tIdentity : ParameterIdentity.values()) { 
//			System.out.println("tIdentity " + tIdentity.toString() + " name " + tIdentity.getName() + " mName = " + mName);
			if (tIdentity.getName().equals(mName)) {
				mIdentity = tIdentity;
				break;
			}
		}
	}
	
	public String getName() { 
		return mIdentity != null ? mIdentity.toString() : mName;
	}
	
	public String getValue() { return mValue; }
	
	public void setValue(String pValue) { mValue = pValue; }

	@Override
	public int compareTo(ParameterEntry o) {
		return getName().compareTo(o.getName());
	}
}
