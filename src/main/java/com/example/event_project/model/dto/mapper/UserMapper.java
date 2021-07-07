package com.example.event_project.model.dto.mapper;


import com.example.event_project.model.User;
import com.example.event_project.model.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    User dtoToUser(UserDto dto);

    UserDto userToDto(User user);

}
