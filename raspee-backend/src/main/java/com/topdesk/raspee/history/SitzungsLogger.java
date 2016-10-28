package com.topdesk.raspee.history;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.topdesk.raspee.entities.Sitzung;
import com.topdesk.raspee.entities.SitzungRepository;
import com.topdesk.raspee.gpio.MyGpioController;
import com.topdesk.raspee.gpio.StateChangeAction;

@Component
public class SitzungsLogger {
	
	private static final Executor executor = Executors.newCachedThreadPool();
	
	private volatile long closingTime;
	@Autowired
	public SitzungsLogger(MyGpioController gpioController, SitzungRepository sitzungRepository) {
		gpioController.addListener(new StateChangeAction() {
			@Override
			public void perform(boolean isActive) {
				if (!isActive) {
					closingTime = System.currentTimeMillis();
				}
				else {
					executor.execute(() -> persistHistoryEntry(sitzungRepository));
				}
			}

			private void persistHistoryEntry(SitzungRepository sitzungRepository) {
				long duration = System.currentTimeMillis() - closingTime;
				Sitzung sitzung = new Sitzung();
				sitzung.setDuration(duration);
				sitzung.setCreationDate(new Date());
				sitzungRepository.save(sitzung);
			}
		});
	}
}
