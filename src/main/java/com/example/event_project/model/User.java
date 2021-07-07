package com.example.event_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String login;
    String password;
    @Column(unique = true)
    String email;
    @Column(unique = true)
    String showName;
    @Enumerated(EnumType.STRING)
    Role role;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @ToString.Exclude
    List<Participant> participant;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @ToString.Exclude
    List<Organizer> organizer;
}
