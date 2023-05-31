package com.ev.evserver.recruiter.surveys;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/surveys",
consumes = MediaType.APPLICATION_JSON_VALUE,
produces = MediaType.APPLICATION_JSON_VALUE)
public class SurveysController {

    private final SurveysService surveysService;

    @Autowired
    public SurveysController(SurveysService surveysService) {
        this.surveysService = surveysService;
    }

    @GetMapping("{code}")
    public ResponseEntity<SurveyDto> getSurvey(@PathVariable String code) {

        SurveyDto surveyDto = surveysService.findByCode(code);

        if (surveyDto != null) {
            return new ResponseEntity<>(surveyDto, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("{id}")
    public ResponseEntity<SurveyDto> modifySurvey(@PathVariable Long id, @Valid @RequestBody SurveyDto surveyDto) {

        SurveyDto newSurveyDto = surveysService.modifySurvey(id, surveyDto);

        if (newSurveyDto != null) {
            return new ResponseEntity<>(surveyDto, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
