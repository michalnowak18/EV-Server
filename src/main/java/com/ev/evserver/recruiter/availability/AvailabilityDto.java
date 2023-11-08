package com.ev.evserver.recruiter.availability;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class AvailabilityDto {

	private Long id;

	@NotNull(message = "Podaj początek dostępności")
	private Timestamp startDate;

	@NotNull(message = "Podaj koniec dostępności")
	private Timestamp endDate;

	private Long eventId;

	public AvailabilityDto() {

	}

	public AvailabilityDto(Availability availability) {
		this.id = availability.getId();
		this.startDate = availability.getStartDate();
		this.endDate = availability.getEndDate();
		this.eventId = availability.getEvent().getId();
	}

	public AvailabilityDto(Long id, Timestamp startDate, Timestamp endDate, Long eventId) {
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.eventId = eventId;
	}

	public AvailabilityDto(Timestamp startDate, Timestamp endDate, Long eventId) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.eventId = eventId;
	}
}
