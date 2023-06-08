package com.ev.evserver.recruiter.surveys;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

import static com.ev.evserver.recruiter.surveys.SurveyState.UNDEFINED;

@Data
public class SurveyDto {

    private Long id;

    private String code;

    private Timestamp date;

    private Long eventId;

    @NotNull
    @Column(name = "survey_state")
    private SurveyState surveyState = UNDEFINED;

    public SurveyDto() {
    }

    public SurveyDto(Survey survey) {
        this.id = survey.getId();
        this.code = survey.getCode();
        this.date = survey.getDate();
        this.eventId = survey.getEvent().getId();
        this.surveyState = survey.getSurveyState();
    }
}
