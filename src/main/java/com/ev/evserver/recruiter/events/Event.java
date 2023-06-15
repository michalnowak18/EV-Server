package com.ev.evserver.recruiter.events;

import com.ev.evserver.recruiter.availability.Availability;
import com.ev.evserver.recruiter.surveys.Survey;
import com.google.common.base.Objects;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.sql.Date;
import java.util.Set;

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

    @Column(name = "slots_taken")
    private Integer slotsTaken = 0;

    @NotNull(message = "Podaj datę rozpoczęcia badań")
    @Column(name = "research_start_date")
    private Date researchStartDate;

    @NotNull(message = "Podaj datę zakończenia badań")
    @Column(name = "research_end_date")
    private Date researchEndDate;

    @Column(name = "is_active")
    private boolean isActive = true;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private Set<Availability> availabilities;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private Set<Survey> surveys;

    public Event() {
    }

    public Event(String name, String description, Date endDate, Integer maxUsers, Float surveyDuration,
                 Float surveyBreakTime, Integer slotsTaken, Date researchStartDate, Date researchEndDate,
                 boolean isActive) {
        this.name = name;
        this.description = description;
        this.endDate = endDate;
        this.maxUsers = maxUsers;
        this.surveyDuration = surveyDuration;
        this.surveyBreakTime = surveyBreakTime;
        this.slotsTaken = slotsTaken;
        this.researchStartDate = researchStartDate;
        this.researchEndDate = researchEndDate;
        this.isActive = isActive;
    }

    public Event(EventDto eventDto) {
        this.id = eventDto.getId();
        this.name = eventDto.getName();
        this.description = eventDto.getDescription();
        this.endDate = eventDto.getEndDate();
        this.maxUsers = eventDto.getMaxUsers();
        this.surveyDuration = eventDto.getSurveyDuration();
        this.surveyBreakTime = eventDto.getSurveyBreakTime();
        this.slotsTaken = eventDto.getSlotsTaken();
        this.researchStartDate = eventDto.getResearchStartDate();
        this.researchEndDate = eventDto.getResearchEndDate();
        this.isActive = eventDto.isActive();
    }

    public boolean isFull() {
        return this.slotsTaken == this.maxUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equal(id, event.id) &&
            Objects.equal(name, event.name) &&
            Objects.equal(description, event.description) &&
            Objects.equal(endDate, event.endDate) &&
            Objects.equal(maxUsers, event.maxUsers) &&
            Objects.equal(surveyDuration, event.surveyDuration) &&
            Objects.equal(surveyBreakTime, event.surveyBreakTime) &&
            Objects.equal(slotsTaken, event.slotsTaken) &&
            Objects.equal(researchStartDate, event.researchStartDate) &&
            Objects.equal(researchEndDate, event.researchEndDate) &&
            Objects.equal(isActive, event.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, description, endDate, maxUsers, surveyDuration, surveyBreakTime, slotsTaken,
            researchStartDate, researchEndDate, isActive);
    }
}