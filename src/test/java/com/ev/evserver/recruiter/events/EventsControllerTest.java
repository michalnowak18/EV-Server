package com.ev.evserver.recruiter.events;

import com.ev.evserver.EvServerApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
                classes = EvServerApplication.class)
@AutoConfigureMockMvc
public class EventsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;
    
    @Test
    public void givenEventsWhenGetEventsThenStatus200() throws Exception {
        createTestEvent("test",Date.valueOf( "2022-01-01"),5,60.0f,
                        15.0f,Date.valueOf( "2022-01-01"),Date.valueOf( "2022-01-02"));

        createTestEvent("test2",Date.valueOf( "2022-01-01"),10,60.0f,
                30.0f,Date.valueOf( "2022-01-01"),Date.valueOf( "2022-01-04"));

        mockMvc.perform(get("/events").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("test")))
                .andExpect(jsonPath("$[1].name", is("test2")));
    }

    @Test
    public void whenValidInputThenCreateEvent() throws Exception {
        EventDto event = new EventDto();
        event.setName("test");
        event.setEndDate(Date.valueOf( "2022-01-10"));
        event.setMaxUsers(5);
        event.setSurveyDuration(70.0f);
        event.setSurveyBreakTime(30.0f);
        event.setResearchStartDate(Date.valueOf( "2022-01-01"));
        event.setResearchEndDate(Date.valueOf( "2022-01-10"));
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(event);

        mockMvc.perform(post("/events").contentType(MediaType.APPLICATION_JSON).content(json));
        List<Event> events = eventRepository.findAll();

        assertThat(events).extracting(Event::getName).containsOnly("test");
    }
    
    private void createTestEvent(String name, Date endDate, Integer maxUsers, Float surveyDuration, 
                                 Float surveyBreakTime, Date researchStartDate, Date researchEndDate ) {
        Event event = new Event();
        event.setName(name);
        event.setEndDate(endDate);
        event.setMaxUsers(maxUsers);
        event.setSurveyDuration(surveyDuration);
        event.setSurveyBreakTime(surveyBreakTime);
        event.setResearchStartDate(researchStartDate);
        event.setResearchEndDate(researchEndDate);
        eventRepository.save(event);
    }
}
