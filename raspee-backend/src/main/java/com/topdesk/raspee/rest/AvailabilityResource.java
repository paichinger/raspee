package com.topdesk.raspee.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topdesk.raspee.dto.AvailabilityDto;
import com.topdesk.raspee.gpio.SimpleStateChangeAction;

@RestController
public class AvailabilityResource {
	
	private final SimpleStateChangeAction stateChangeAction;
	
	@Autowired
	public AvailabilityResource(SimpleStateChangeAction stateChangeAction) {
		this.stateChangeAction = stateChangeAction;
	}

	@RequestMapping("/available")
	public AvailabilityDto isAvailable() {
		return new AvailabilityDto(stateChangeAction.isActive());
	}
	

}
