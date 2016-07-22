package com.topdesk.raspee.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topdesk.raspee.dto.AvailabilityDto;
import com.topdesk.raspee.gpio.GpioListener;

@RestController
public class AvailabilityResource {
	
	private final GpioListener gpio;
	
	@Autowired
	public AvailabilityResource(GpioListener gpio) {
		this.gpio = gpio;
	}

	@RequestMapping("/available")
	public AvailabilityDto isAvailable() {
		return new AvailabilityDto(gpio.isReleased());
	}
	

}
