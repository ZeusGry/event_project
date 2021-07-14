package com.example.event_project.repository;

import com.example.event_project.model.Event;
import com.example.event_project.model.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizerRepository extends JpaRepository<Organizer, Long> {
    List<Event> findByUserId(Long id);
}
