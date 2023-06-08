package com.ev.evserver.recruiter.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EventsUtils {
    EventRepository eventRepository;

    @Autowired
    public EventsUtils(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event fetchValidEvent(long id) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (eventOpt.isEmpty()) {
            throw new RuntimeException("Invalid ID");
        }

        return eventOpt.get();
    }

}
