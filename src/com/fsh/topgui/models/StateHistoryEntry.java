package com.fsh.topgui.models;
/** Copyright EV112 Development AB, Stockholm, Sweden 2010 */

/**
 * Simple class to manage a single state history entry, simply data container
 */
public class StateHistoryEntry {

	// Holds one state history entry
	final String mTime;
	final String mState;
	final String mSubState;
	final String mLastReason;
	
	public StateHistoryEntry(String pTime, String pState, String pSubState, String pLastReason) {
		mTime = pTime;
		mState = pState;
		mSubState = pSubState;
		mLastReason = pLastReason;
	}
	
}
