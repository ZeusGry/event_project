package com.example.event_project.model.dto;

import com.example.event_project.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Data
public class UserDto {
    Long id;
    @JsonIgnore
    @ToString.Exclude
    String password;
    String login;
    Set<Role> roles;
    String email;
    String showName;
}
