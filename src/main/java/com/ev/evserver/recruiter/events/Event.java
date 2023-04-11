package com.ev.evserver.recruiter.events;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Podaj nazwę wydarzenia")
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull(message = "Podaj datę rozpoczęcia")
    @Column(name = "start_date")
    private Date startDate;

    @NotNull(message = "Podaj datę zakończenia")
    @Column(name = "end_date")
    private Date endDate;

    @NotNull(message = "Podaj maksymalną liczbę uczestników")
    @Column(name = "max_users")
    private Integer maxUsers;

    @NotNull(message = "Podaj długość spotkania")
    @Column(name = "duration")
    private Float duration;

    @NotNull(message = "Podaj długość przerwy")
    @Column(name = "break_time")
    private Float breakTime;

    public Event() {
    }
}