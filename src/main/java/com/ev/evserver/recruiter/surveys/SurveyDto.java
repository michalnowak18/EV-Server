package com.ev.evserver.recruiter.surveys;

import lombok.Data;

import java.sql.Date;

@Data
public class SurveyDto {

    private Long id;

    private String code;

    private Date date;

    public SurveyDto() {
    }

    public SurveyDto(Survey survey) {
        this.id = survey.getId();
        this.code = survey.getCode();
        this.date = survey.getDate();
    }
}
