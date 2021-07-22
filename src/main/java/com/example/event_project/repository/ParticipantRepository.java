package com.example.event_project.repository;

import com.example.event_project.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findByUserId(Long id);

    Optional<Participant> findByUserIdAndEventId(Long userID, Long eventId);

}
