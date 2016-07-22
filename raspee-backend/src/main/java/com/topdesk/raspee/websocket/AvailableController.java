package com.topdesk.raspee.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.topdesk.raspee.dto.AvailabilityDto;
import com.topdesk.raspee.gpio.GpioListener;

@Controller
public class AvailableController {
	
	private final GpioListener gpio;
	
	@Autowired
	public AvailableController(GpioListener gpio) {
		this.gpio = gpio;
	}

    @MessageMapping("/available")
    @SendTo("/topic/available")
    public AvailabilityDto available() throws Exception {
    	return new AvailabilityDto(gpio.isReleased());
    }

}