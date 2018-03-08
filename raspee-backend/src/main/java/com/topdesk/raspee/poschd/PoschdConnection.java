package com.topdesk.raspee.poschd;

import com.topdesk.poschd.client.PoschdClient;
import com.topdesk.poschd.client.PoschdProducer;
import com.topdesk.poschd.client.ProducerConfig;
import com.topdesk.poschd.client.TopicConfig;
import com.topdesk.raspee.gpio.MyGpioController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PoschdConnection {
	
	private final PoschdClient poschdClient;
	private final MyGpioController gpioController;

	@PostConstruct
	public void init() {
		PoschdProducer<String> producer = poschdClient.createProducer(
				ProducerConfig.of(
						key -> null,
						TopicConfig.of("klo", String.class)
				).withFaultTolerance(true));
		
		gpioController.addListener(doorClosed -> {
			producer.send(String.valueOf(doorClosed));
		});
	}
}
