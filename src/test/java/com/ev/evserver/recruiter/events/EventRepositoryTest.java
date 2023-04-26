package com.ev.evserver.recruiter.events;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EventRepositoryTest {
    @Autowired
    private EventRepository eventRepository;

    @Test
    public void givenCreateEventWhenLoadTheEventThenExpectSameEvent() {
        Event event = new Event(
                "eventTest",
                "test",
                Date.valueOf( "2022-01-01"),
                4,
                60.0f,
                30.0f,
                0,
                Date.valueOf("2022-02-02"),
                Date.valueOf("2022-02-22"));
        Event savedEvent = eventRepository.save(event);
        assertThat(eventRepository.findById(savedEvent.getId()).get()).isEqualTo(event);
        eventRepository.delete(savedEvent);
    }

    @Test
    public void givenFindAllWhenLoadEventsThenExpectNumberOfEvents() {
        saveMockEvents();
        assertThat(eventRepository.findAll()).hasSize(3);
    }

    private void saveMockEvents(){
        Event event1 = new Event(
                "eventTest",
                "test",
                Date.valueOf( "2022-01-01"),
                4,
                60.0f,
                30.0f,
                0,
                Date.valueOf("2022-02-02"),
                Date.valueOf("2022-02-22"));

        Event event2 = new Event(
                "eventTest",
                "test",
                Date.valueOf( "2022-01-01"),
                4,
                60.0f,
                30.0f,
                0,
                Date.valueOf("2022-02-02"),
                Date.valueOf("2022-02-22"));

        Event event3 = new Event(
                "eventTest",
                "test",
                Date.valueOf( "2022-01-01"),
                4,
                60.0f,
                30.0f,
                0,
                Date.valueOf("2022-02-02"),
                Date.valueOf("2022-02-22"));

        List<Event> events = Arrays.asList(event1,event2,event3);
        eventRepository.saveAll(events);
    }

    @AfterAll
    public void clearDatabaseAfterAll() {
        eventRepository.deleteAll();
    }
}
