package com.topdesk.raspee.gpio;

public interface StateChangeAction {
	void perform(boolean isActive);
}
