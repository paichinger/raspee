package com.topdesk.raspee.gpio;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SessionListener implements StateChangeAction {
	@Getter private boolean active;
	
	@Autowired
	public SessionListener(MyGpioController gpioController) {
		gpioController.addListener(this);
	}
	
	@Override
	public void perform(boolean isActive) {
		this.active = isActive;
		System.out.println("Action performed: " + active);
	}
}