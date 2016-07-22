package com.topdesk.raspee.gpio;

import lombok.Getter;

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
import com.topdesk.raspee.dto.AvailabilityDto;
import com.topdesk.raspee.entities.Sitzung;
import com.topdesk.raspee.entities.SitzungRepository;

@Component
public class GpioListener {
	
	private final GpioController gpio = GpioFactory.getInstance();
	private GpioPinDigitalInput pin2;
	private volatile PinState currentState;
	@Getter private long lastTimePressed;
	
	@Autowired
	public GpioListener(final SitzungRepository sitzungRepository, SimpMessagingTemplate template) {
		changeValue(template);
		gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "MyLED", PinState.HIGH);

		pin2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);
		pin2.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                template.convertAndSend("/topic/available", new AvailabilityDto(isReleased()));
                currentState = event.getState();
                if (!isReleased()) {
                	lastTimePressed = System.currentTimeMillis();
                }
                else {
                	Sitzung sitzung = new Sitzung();
                	sitzung.setDuration(System.currentTimeMillis() - lastTimePressed);
                	sitzungRepository.save(sitzung);
                }
            }
        });
	}
	
	private void changeValue(SimpMessagingTemplate template) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean value = true;
				while(true) {
					template.convertAndSend("/topic/available", new AvailabilityDto(value));
					System.out.println(value);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					value = !value;
				}
			}
		}).start();
	}
	
	public boolean isReleased() {
		return currentState == PinState.LOW;
	}
	
}