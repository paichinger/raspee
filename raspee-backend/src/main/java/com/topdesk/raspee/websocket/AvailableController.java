package com.topdesk.raspee.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.topdesk.raspee.dto.AvailabilityDto;
import com.topdesk.raspee.gpio.MyGpioController;
import com.topdesk.raspee.gpio.SimpleStateChangeAction;
import com.topdesk.raspee.gpio.StateChangeAction;

@Controller
public class AvailableController {
	
	private final SimpleStateChangeAction action;

	@Autowired
	public AvailableController(SimpleStateChangeAction action, MyGpioController gpioController, SimpMessagingTemplate template) {
		this.action = action;
		gpioController.addListener(new StateChangeAction() {
			@Override
			public void perform(boolean isActive) {
				template.convertAndSend("/topic/available",	new AvailabilityDto(isActive));
			}
		});
	}

    @MessageMapping("/available")
    @SendTo("/topic/available")
    public AvailabilityDto available() throws Exception {
    	return new AvailabilityDto(!action.isActive());
    }

}