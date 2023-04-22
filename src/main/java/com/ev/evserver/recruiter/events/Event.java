package com.ev.evserver.recruiter.events;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Podaj nazwę wydarzenia")
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull(message = "Podaj datę zakończenia zapisów")
    @Column(name = "end_date")
    private Date endDate;

    @NotNull(message = "Podaj maksymalną liczbę uczestników")
    @Column(name = "max_users")
    private Integer maxUsers;

    @NotNull(message = "Podaj długość spotkania")
    @Column(name = "survey_duration")
    private Float surveyDuration;

    @NotNull(message = "Podaj długość przerwy")
    @Column(name = "survey_break_time ")
    private Float surveyBreakTime;

    @Column(name = "slots_taken", columnDefinition = "integer default 0")
    private Integer slotsTaken;

    @NotNull(message = "Podaj datę rozpoczęcia badań")
    @Column(name = "research_start_date")
    private Date researchStartDate;

    @NotNull(message = "Podaj datę zakończenia badań")
    @Column(name = "research_end_date")
    private Date researchEndDate;

    public Event() {
    }
}