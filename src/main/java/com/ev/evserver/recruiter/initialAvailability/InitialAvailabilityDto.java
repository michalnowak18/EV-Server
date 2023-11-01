package com.ev.evserver.recruiter.initialAvailability;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class InitialAvailabilityDto {

    private Long id;

    @NotNull
    private Timestamp startDate;

    @NotNull
    private Timestamp endDate;

    private Long eventId;

    public InitialAvailabilityDto() {

    }

    public InitialAvailabilityDto(InitialAvailability initialAvailability) {
        this.id = initialAvailability.getId();
        this.startDate = initialAvailability.getStartDate();
        this.endDate = initialAvailability.getEndDate();
        this.eventId = initialAvailability.getEvent().getId();
    }

}
