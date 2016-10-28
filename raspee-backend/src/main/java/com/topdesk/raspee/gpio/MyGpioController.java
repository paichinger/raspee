package com.topdesk.raspee.gpio;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

@Component
public class MyGpioController {

	private GpioController gpio;
	private GpioPinDigitalInput pin;
	private volatile PinState currentState;
	private boolean fromWorkspace;

	@Autowired
	public MyGpioController(SimpMessagingTemplate template) {
		fromWorkspace = "TRUE".equals(System.getProperty("WORKSPACE"));
		if (!fromWorkspace) {
			gpio = GpioFactory.getInstance();
			gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "MyLED",
					PinState.HIGH);

			pin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04,
					PinPullResistance.PULL_DOWN);
		}
	}

	public void addListener(StateChangeAction action) {
		if(!fromWorkspace) {
			pin.addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
					action.perform(event.getState() == PinState.HIGH);
				}
			});
		}
		else {
			new Thread(new TestRunner(action)).start();
		}
	}

	public boolean isReleased() {
		return currentState == PinState.LOW;
	}

	@RequiredArgsConstructor
	private static class TestRunner implements Runnable {
		private boolean currentState;
		private final StateChangeAction action;
		public void run() {
			while (true) {
				action.perform(currentState);
				currentState = !currentState;
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}