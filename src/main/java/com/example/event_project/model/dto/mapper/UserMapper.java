package com.example.event_project.model.dto.mapper;


import com.example.event_project.model.User;
import com.example.event_project.model.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface UserMapper {
    User dtoToUser(UserDto dto);

    @Mappings({
            @Mapping(target = "roles", source = "user.roles")})
    UserDto userToDto(User user);

}
