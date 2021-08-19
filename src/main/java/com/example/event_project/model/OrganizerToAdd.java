package com.example.event_project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizerToAdd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Event event;
    private String userEmail;

    public OrganizerToAdd(Event event, String userEmail) {
        this.event = event;
        this.userEmail = userEmail;
    }
}
