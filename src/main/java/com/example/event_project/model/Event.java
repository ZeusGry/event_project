package com.example.event_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String name;
    LocalDateTime startTime;
    @ManyToOne
    Adress adress;
    Boolean acces;
    String email;
    String phoneNumber;
    @OneToMany(mappedBy = "event")
    @JsonIgnore
    @ToString.Exclude
    List<Organizer> organizers;
    @OneToMany(mappedBy = "event")
    @JsonIgnore
    @ToString.Exclude
    List<Participant> participants;
    @OneToMany
    @JsonIgnore
    @ToString.Exclude
    List<Comment> comments;
}
