package com.ev.evserver.recruiter.surveys;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class SurveyDto {

    private Long id;

    private String code;

    private Timestamp date;

    private Long eventId;

    public SurveyDto() {
    }

    public SurveyDto(Survey survey) {
        this.id = survey.getId();
        this.code = survey.getCode();
        this.date = survey.getDate();
        this.eventId = survey.getEvent().getId();
    }
}
