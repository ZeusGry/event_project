package com.example.event_project.repository;

import com.example.event_project.model.Event;
import com.example.event_project.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Event> findByUserIdAndAccepted(Long id, Boolean isAccepted);
}
