package com.ev.evserver.recruiter.events;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Date;

@Data
public class EventDto {

	private Long id;

	@NotBlank(message = "Podaj nazwę wydarzenia")
	private String name;

	private String description;

	@NotNull(message = "Podaj datę zakończenia zapisów")
	private Date endDate;

	@NotNull(message = "Podaj maksymalną liczbę uczestników")
	private Integer maxUsers;

	@NotNull(message = "Podaj długość spotkania")
	private Float surveyDuration;

	@NotNull(message = "Podaj długość przerwy")
	private Float surveyBreakTime;

	private Integer slotsTaken = 0;

	@NotNull(message = "Podaj datę rozpoczęcia badań")
	private Date researchStartDate;

	@NotNull(message = "Podaj datę zakończenia badań")
	private Date researchEndDate;

	public EventDto() {
	}

	public EventDto(Event event) {
		this.id = event.getId();
		this.name = event.getName();
		this.description = event.getDescription();
		this.endDate = event.getEndDate();
		this.maxUsers = event.getMaxUsers();
		this.surveyDuration = event.getSurveyDuration();
		this.surveyBreakTime = event.getSurveyBreakTime();
		this.slotsTaken = event.getSlotsTaken();
		this.researchStartDate = event.getResearchStartDate();
		this.researchEndDate = event.getResearchEndDate();
	}
}
