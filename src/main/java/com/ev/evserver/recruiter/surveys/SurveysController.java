package com.ev.evserver.recruiter.surveys;

import com.ev.evserver.recruiter.events.Event;
import com.ev.evserver.recruiter.events.EventsUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(
consumes = MediaType.APPLICATION_JSON_VALUE,
produces = MediaType.APPLICATION_JSON_VALUE)
public class SurveysController {

    private final SurveysService surveysService;

    private final EventsUtils eventsUtils;

    @Autowired
    public SurveysController(SurveysService surveysService, EventsUtils eventsUtils) {
        this.surveysService = surveysService;
        this.eventsUtils = eventsUtils;
    }

    @GetMapping("/surveys/{code}")
    public ResponseEntity<SurveyDto> getSurvey(@PathVariable String code) {

        SurveyDto surveyDto = surveysService.findByCode(code);

        if (surveyDto != null) {
            return new ResponseEntity<>(surveyDto, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/events/{eventId}/surveys")
    public ResponseEntity<List<SurveyDto>> getAllSurveys(@PathVariable Long eventId) {

        List<SurveyDto> surveyDto = surveysService.findByEvent(eventId);

        return new ResponseEntity<>(surveyDto, HttpStatus.OK);

    }

    @GetMapping("/surveys/export")
    public void exportAllSurveysToCSV(@PathVariable Long eventId,
                                      HttpServletResponse response) throws IOException {

        response.setContentType("text/csv");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd_HH:mm:ss");
        LocalDateTime currentDateTime = LocalDateTime.now();

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=surveys_" + formatter.format(currentDateTime) + ".csv";
        response.setHeader(headerKey, headerValue);
        response.setHeader(headerKey, "Access-Control-Expose-Headers");

        List<SurveyDto> listOfSurveys = surveysService.findByEvent(eventId);

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
        String[] csvHeader = {"Survey ID", "Code", "Date", "Event ID", "Event Name", "State", "Consents"};
        String[] nameMapping = {"id", "code", "date", "eventId", "eventName", "surveyState", "consentsIds"};

        csvWriter.writeHeader(csvHeader);

        for (SurveyDto surveyDto : listOfSurveys) {

            csvWriter.write(surveyDto, nameMapping);
        }

        csvWriter.close();

    }

    @PatchMapping("/surveys/{id}")
    public ResponseEntity<SurveyDto> modifySurvey(@Valid @RequestBody SurveyDto surveyDto,
                                                  @PathVariable Long id) {

        SurveyDto newSurveyDto = surveysService.modifySurvey(id, surveyDto);

        return new ResponseEntity<>(newSurveyDto, HttpStatus.OK);
    }

    @PostMapping("/events/{eventId}/surveys")
    public ResponseEntity<SurveyDto> generateSurvey(@PathVariable Long eventId) {

        Event event = eventsUtils.fetchValidEvent(eventId);
        List<Survey> survey = surveysService.saveSurveyWithGeneratedSlots(1, event);

        SurveyDto surveyDto = new SurveyDto(survey.get(0));

        return new ResponseEntity<>(surveyDto, HttpStatus.OK);
    }
}
