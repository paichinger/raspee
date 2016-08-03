package com.topdesk.raspee.poschd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.topdesk.poschd.client.PoschdClient;
import com.topdesk.poschd.client.PoschdFactory;
import com.topdesk.poschd.client.PoschdProducer;
import com.topdesk.poschd.client.ProducerConfig;
import com.topdesk.poschd.client.TopicConfig;
import com.topdesk.raspee.gpio.MyGpioController;
import com.topdesk.raspee.gpio.StateChangeAction;
import com.topdesk.tbd.FakeConfigService;
import com.topdesk.tbd.FakeConfigValues;

@Controller
public class PoschdConnection {
	
	@Autowired
	public PoschdConnection(MyGpioController gpioController) {
		FakeConfigValues.POSCHD_CONNECTION_URL.setValue("tcp://de-test-centos:21616?connectionTimeout=2000");
		PoschdClient poschd = new PoschdFactory().createPoschd();
		PoschdProducer<String> producer = poschd.createProducer(ProducerConfig.of(new FakeConfigService(), TopicConfig.of("klo", String.class)).withFaultTolerance(true).withStorageDir("senderStorage"));
		
		gpioController.addListener(new StateChangeAction() {
			@Override
			public void perform(boolean isActive) {
				producer.send(String.valueOf(isActive));
				System.out.println("Sending to Poschd: " + isActive);
			}
		});
	}
	
}
