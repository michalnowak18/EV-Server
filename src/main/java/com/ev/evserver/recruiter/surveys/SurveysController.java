package com.ev.evserver.recruiter.surveys;

import com.ev.evserver.consent.ConsentDto;
import com.ev.evserver.consent.ConsentService;
import com.ev.evserver.recruiter.events.Event;
import com.ev.evserver.recruiter.events.EventsUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(
consumes = MediaType.APPLICATION_JSON_VALUE,
produces = MediaType.APPLICATION_JSON_VALUE)
public class SurveysController {

    private final SurveysService surveysService;

    private final EventsUtils eventsUtils;

    private final ConsentService consentService;

    @Autowired
    public SurveysController(SurveysService surveysService, EventsUtils eventsUtils, ConsentService consentService) {
        this.surveysService = surveysService;
        this.eventsUtils = eventsUtils;
        this.consentService = consentService;
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

    @GetMapping("/surveys/export/{eventId}")
    public void exportAllSurveysToCSV(@PathVariable Long eventId,
                                      HttpServletResponse response) throws IOException {

        List<SurveyDto> listOfSurveys = surveysService.findByEvent(eventId);
        List<ConsentDto> consentDtoList = consentService.getAllByEvent(eventId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd_HH_mm_ss");
        LocalDateTime currentDateTime = LocalDateTime.now();

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=event_" + eventId + "_" + formatter.format(currentDateTime) + ".csv";

        response.setHeader(headerKey, headerValue);
        response.setHeader("Access-Control-Expose-Headers", headerKey);
        response.setContentType("text/csv");

        PrintWriter writer = response.getWriter();
        List<String> row = new ArrayList<>();

        Integer numberOfConsents = consentDtoList.size();

        // add header

        row.add("ID spotkania");
        row.add("Kod spotkania");
        row.add("Data spotkania");
        row.add("Nazwa eventu");
        row.add("Stan spotkania");

        for (int i = 0; i < numberOfConsents; i++) {
            row.add("Zgoda %s".formatted(i + 1));
            row.add("Zaakceptowana");
        }
        writer.println(String.join(";", row));
        row.clear();

        for (SurveyDto surveyDto : listOfSurveys) {

            // add data
            row.add(surveyDto.getId().toString());
            row.add(surveyDto.getCode());
            if (surveyDto.getDate() != null) {
                row.add(surveyDto.getDate().toString());
            } else {
                row.add("");
            }
            row.add(surveyDto.getEventName());
            row.add(surveyDto.getSurveyState().toString());

            for (ConsentDto consentDto : consentDtoList) {
                row.add(consentDto.getContent());
                row.add(consentService.findAcceptedConsents(consentDto.getId(), surveyDto.getId()));
            }

            writer.println(String.join(";", row));
            row.clear();
        }

        writer.close();
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
