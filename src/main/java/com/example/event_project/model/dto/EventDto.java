package com.example.event_project.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDto {
    Long id;
    String name;
    LocalDateTime startTime;
    Boolean acces;
    String email;
    String phoneNumber;
    Integer participantCount;
    Integer commentCount;
}
