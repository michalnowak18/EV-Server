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
}
