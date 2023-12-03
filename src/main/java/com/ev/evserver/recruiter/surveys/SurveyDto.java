package com.ev.evserver.recruiter.surveys;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

import static com.ev.evserver.recruiter.surveys.SurveyState.UNUSED;

@Data
public class SurveyDto {

    private Long id;

    private String code;

    private Timestamp date;

    private Long eventId;

    private String eventName;

    @NotNull
    @Column(name = "survey_state")
    private SurveyState surveyState = UNUSED;

    private List<Long> consentsIds;

    public SurveyDto() {
    }

    public SurveyDto(Survey survey) {
        this.id = survey.getId();
        this.code = survey.getCode();
        this.date = survey.getDate();
        this.eventId = survey.getEvent().getId();
        this.eventName = survey.getEvent().getName();
        this.surveyState = survey.getSurveyState();
        this.consentsIds = survey.getConsentsIds();
    }
}
