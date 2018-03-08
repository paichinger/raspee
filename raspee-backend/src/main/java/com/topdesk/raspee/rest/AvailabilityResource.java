package com.topdesk.raspee.rest;

import com.topdesk.raspee.dto.AvailabilityDto;
import com.topdesk.raspee.gpio.MyGpioController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AvailabilityResource {
	private final MyGpioController gpioController;
	private volatile Boolean doorClosed;
	
	@PostConstruct
	public void init() {
		gpioController.addListener(closed -> doorClosed = closed);
	}

	@RequestMapping("/available")
	public AvailabilityDto isAvailable() {
		return new AvailabilityDto(doorClosed);
	}

}
