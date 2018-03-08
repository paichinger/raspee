package com.topdesk.raspee.history;

import com.topdesk.raspee.entities.Sitzung;
import com.topdesk.raspee.entities.SitzungRepository;
import com.topdesk.raspee.gpio.MyGpioController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
public class SitzungsLogger {
	
	private static final Executor executor = Executors.newCachedThreadPool();
	
	private volatile long closingTime;
	@Autowired
	public SitzungsLogger(MyGpioController gpioController, SitzungRepository sitzungRepository) {
		gpioController.addListener(doorClosed -> {
			if (!doorClosed) {
				closingTime = System.currentTimeMillis();
			}
			else {
				executor.execute(() -> persistHistoryEntry(sitzungRepository));
			}

		});
	}
	private void persistHistoryEntry(SitzungRepository sitzungRepository) {
		long duration = System.currentTimeMillis() - closingTime;
		Sitzung sitzung = new Sitzung();
		sitzung.setDuration(duration);
		sitzung.setCreationDate(new Date());
		sitzungRepository.save(sitzung);
	}
}
