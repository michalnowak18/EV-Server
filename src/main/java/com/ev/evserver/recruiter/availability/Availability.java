package com.ev.evserver.recruiter.availability;

import com.ev.evserver.recruiter.events.Event;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "availability")
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Podaj początek dostępności")
    @Column(name = "start_date")
    private Timestamp startDate;

    @NotNull(message = "Podaj koniec dostępności")
    @Column(name = "end_date")
    private Timestamp endDate;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event eventId;

    public Availability() {
    }
}