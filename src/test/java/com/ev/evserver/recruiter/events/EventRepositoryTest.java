package com.ev.evserver.recruiter.events;

import com.ev.evserver.user.User;
import com.ev.evserver.user.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void givenCreateEventWhenLoadTheEventThenExpectSameEvent() {

        Event event = createEvent("Event");
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

        Event event = createEvent(RandomStringUtils.randomAlphabetic(8));

        List<Event> events = Arrays.asList(
            createEvent("Event1"),
            createEvent("Event2"),
            createEvent("Event3"));

        eventRepository.saveAll(events);
    }

    @AfterAll
    public void clearDatabaseAfterAll() {
        eventRepository.deleteAll();
    }

    private Event createEvent(String name) {

        Event event = new Event(
            name,
            "test",
            Date.valueOf( "2022-01-01"),
            4,
            60.0f,
            30.0f,
            0,
            Date.valueOf("2022-02-02"),
            Date.valueOf("2022-02-22"),
            createUser());

        return event;
    }

    private User createUser() {

        User user = new User();
        userRepository.save(user);

        return user;
    }
}
