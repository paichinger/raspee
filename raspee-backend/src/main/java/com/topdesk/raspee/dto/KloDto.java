package com.topdesk.raspee.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class KloDto {
	private final boolean available;
	private final long duration;
	private final int dailyCounter;
	private final int overallCounter;
}
