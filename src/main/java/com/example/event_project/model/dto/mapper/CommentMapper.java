package com.example.event_project.model.dto.mapper;

import com.example.event_project.model.Comment;
import com.example.event_project.model.dto.CommentDto;
import org.mapstruct.Mapper;

@Mapper
public interface CommentMapper {
    Comment dtoToComment(CommentDto dto);

    CommentDto commentToDto(Comment comment);
}
