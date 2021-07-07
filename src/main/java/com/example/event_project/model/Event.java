package com.example.event_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Formula;

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
    private Long id;
    @Column(unique = true)
    private String name;
    private LocalDateTime startTime;
    @ManyToOne(fetch = FetchType.EAGER)
    private Adress adress;
    private Boolean acces;
    private String email;
    private String phoneNumber;
    @OneToMany(mappedBy = "event")
    @JsonIgnore
    @ToString.Exclude
    private List<Organizer> organizers;
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Participant> participants;
    @Formula("(SELECT COUNT(*) from participant WHERE participant.event_id = id)")
    private Integer participantCount;
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Comment> comments;
    @Formula("(SELECT COUNT(*) from comment WHERE comment.event_id = id)")
    private Integer commentCount;

}
