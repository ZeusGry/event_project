package com.example.event_project.repository;

import com.example.event_project.model.OrganizerToAdd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizerToAddRepository extends JpaRepository<OrganizerToAdd, Long> {
    List<OrganizerToAdd> findAllByUserEmail(String email);

    void deleteByUserEmail(String email);
}
