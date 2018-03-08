package com.topdesk.raspee.gpio;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

@Slf4j
@Component
public class MyGpioController {

	private Collection<Consumer<Boolean>> listeners = new ArrayList<>();

	@PostConstruct
	public void init() {
		boolean fromWorkspace = "TRUE" .equals(System.getProperty("WORKSPACE"));
		if (!fromWorkspace) {
			GpioController gpio = GpioFactory.getInstance();
			gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "MyLED",
					PinState.HIGH);

			GpioPinDigitalInput pin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04,
					PinPullResistance.PULL_DOWN);
			pin.setDebounce(500);
			pin.addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
					boolean doorClosed = event.getState() == PinState.HIGH;
					listeners.forEach(listener -> listener.accept(doorClosed));
				}
			});
		} else {
			new Thread(new TestRunner()).start();
		}
	}

	public void addListener(Consumer<Boolean> listener) {
		listeners.add(listener);
	}

	@RequiredArgsConstructor
	private class TestRunner implements Runnable {
		private boolean currentState;
		@Override
		public void run() {
			while (true) {
				currentState = !currentState;
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					log.warn("trouble simulating state change", e);
				}
				listeners.forEach(listener -> listener.accept(currentState));
			}
		}
	}
	
}