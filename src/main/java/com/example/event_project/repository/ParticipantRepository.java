package com.example.event_project.repository;

import com.example.event_project.model.Participant;
import com.example.event_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByUserIdAndAccepted(Long id, Boolean isAccepted);

    List<Participant> findByUserId(Long id);

    Optional<Participant> findByUserIdAndEventId(Long userID, Long eventId);

    void deleteByUserId(Long userId);

    void deleteByUser(User user);

}
