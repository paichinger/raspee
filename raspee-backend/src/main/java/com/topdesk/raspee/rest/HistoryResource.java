package com.topdesk.raspee.rest;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import lombok.RequiredArgsConstructor;
import lombok.Value;

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
	public List<SitzungDto> history() {
		return StreamSupport.stream(sitzungRepository.findAll().spliterator(), false)
			.map(s -> new SitzungDto(s.getId(), s.getDuration(), s.getCreationDate()))
			.collect(Collectors.toList());
	}
	
	@RequestMapping("/history/shitlist")
	public List<Sitzung> shitlist() {
		return sitzungRepository.findTop10ByOrderByDurationDesc();
	}

	@RequiredArgsConstructor
	@Value
	private static class SitzungDto {
		private final Long id;
		private final long duration;
		private final Date creationDate;
	}
}
