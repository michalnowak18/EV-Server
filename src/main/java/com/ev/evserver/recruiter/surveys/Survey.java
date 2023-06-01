package com.ev.evserver.recruiter.surveys;

import com.ev.evserver.recruiter.events.Event;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "survey")
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "date")
    private Timestamp date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_event", nullable = false)
    private Event event;

    public Survey() {
    }

    public Survey(String code, Event event) {
        this.code = code;
        this.event = event;
    }

    public Survey(SurveyDto surveyDto) {
        this.id = surveyDto.getId();
        this.code = surveyDto.getCode();
        this.date = surveyDto.getDate();
    }

}