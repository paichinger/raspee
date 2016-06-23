package com.topdesk.raspee.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topdesk.raspee.entities.Sitzung;
import com.topdesk.raspee.entities.SitzungRepository;

@RestController
public class HistoryResource {
	
	private final SitzungRepository sitzungRepository;

	@Autowired
	public HistoryResource(SitzungRepository sitzungRepository) {
		this.sitzungRepository = sitzungRepository;
	}
	
	@RequestMapping("/history")
	public List<Sitzung> history() {
		List<Sitzung> history = new ArrayList<>();
		sitzungRepository.findAll().forEach(e -> history.add(e));
		return history;
	}

}
