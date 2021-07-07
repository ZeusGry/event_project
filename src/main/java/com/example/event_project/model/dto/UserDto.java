package com.example.event_project.model.dto;

import com.example.event_project.model.Role;
import lombok.Data;

@Data
public class UserDto {
    Long id;
    String login;
    String password;
    private Role role;
    String email;
    String showName;
}
