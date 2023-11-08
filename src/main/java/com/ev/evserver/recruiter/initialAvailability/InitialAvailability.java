package com.ev.evserver.recruiter.initialAvailability;

import com.ev.evserver.recruiter.availability.AvailabilityDto;
import com.ev.evserver.recruiter.events.Event;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "initial_availability")
public class InitialAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "start_date")
    private Timestamp startDate;

    @NotNull
    @Column(name = "end_date")
    private Timestamp endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_event", nullable = false)
    private Event event;

    public InitialAvailability() {
    }

    public InitialAvailability(AvailabilityDto availabilityDto) {
        this.startDate = availabilityDto.getStartDate();
        this.endDate = availabilityDto.getEndDate();
    }
}
