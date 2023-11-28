package com.ev.evserver.recruiter.events;

import com.ev.evserver.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {

	Set<Event> findByUser(User user);
}
