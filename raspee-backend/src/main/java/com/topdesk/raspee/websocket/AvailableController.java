package com.topdesk.raspee.websocket;

import com.topdesk.raspee.dto.AvailabilityDto;
import com.topdesk.raspee.gpio.MyGpioController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AvailableController {
	
	private final MyGpioController gpioController;
	private final SimpMessagingTemplate template;
	private AvailabilityDto lastDto;

	@PostConstruct
	public void init() {
		gpioController.addListener(doorClosed -> {
			lastDto = new AvailabilityDto(doorClosed);
			template.convertAndSend("/topic/available", lastDto);
		});
	}

}