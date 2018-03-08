package com.topdesk.raspee.gpio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingListener {

	@Autowired
	public LoggingListener(MyGpioController gpioController) {
		gpioController.addListener(this::perform);
	}
	
	public void perform(boolean doorClosed) {
		log.info("Door closed: {}", doorClosed);
	}
}