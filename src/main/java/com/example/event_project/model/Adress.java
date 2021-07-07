package com.example.event_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Adress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private String street;
    private String numberOfBuilding;
    @OneToMany(mappedBy = "adress", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Event> event;
}
