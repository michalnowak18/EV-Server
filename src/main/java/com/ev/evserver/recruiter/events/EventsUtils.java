package com.ev.evserver.recruiter.events;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventsUtils {
    EventRepository eventRepository;

    public Event fetchValidEvent(long id) {

        Optional<Event> eventOpt = eventRepository.findById(id);
        if (eventOpt.isEmpty()) {
            throw new RuntimeException("Invalid ID");
        }

        return eventOpt.get();
    }

}
