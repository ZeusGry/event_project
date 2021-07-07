package com.example.event_project.model.dto;

import com.example.event_project.model.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    Long id;
    User user;
    LocalDateTime postTime;
    String content;
}
