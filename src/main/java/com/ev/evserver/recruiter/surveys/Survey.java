package com.ev.evserver.recruiter.surveys;

import com.ev.evserver.consent.Consent;
import com.ev.evserver.recruiter.events.Event;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

import static com.ev.evserver.recruiter.surveys.SurveyState.UNUSED;

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

    @NotNull
    @Column(name = "survey_state")
    private SurveyState surveyState = UNUSED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_event", nullable = false)
    private Event event;

    @ManyToMany(mappedBy = "collectionOfSurveys",
            cascade = CascadeType.ALL)
    private List<Consent> consents;

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
        this.surveyState = surveyDto.getSurveyState();
    }

}