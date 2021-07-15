package com.example.event_project.service;

import com.example.event_project.model.Comment;
import com.example.event_project.model.Event;
import com.example.event_project.model.dto.CommentDto;
import com.example.event_project.model.dto.mapper.CommentMapper;
import com.example.event_project.repository.CommentRepository;
import com.example.event_project.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final EventRepository eventRepository;


    public List<CommentDto> getComments(Long id) {
        return commentRepository.findCommentByEvent_IdOrderByPostTime(id)
                .stream()
                .map(comment -> commentMapper.commentToDto(comment))
                .collect(Collectors.toList());
    }

    public CommentDto addComment(Long eventId, CommentDto dto) {
        Optional<Event> optEvent = eventRepository.findById(eventId);
        if (optEvent.isPresent()) {
            Comment comment = commentMapper.dtoToComment(dto);
            comment.setEvent(optEvent.get());
            comment.setUser(null); // małe rozwiązanie na później
            comment.setPostTime(LocalDateTime.now());
            comment = commentRepository.save(comment);
            return commentMapper.commentToDto(comment);
        }
        throw new RuntimeException();
    }

    public void deleteComment(CommentDto dto) {
        Optional<Comment> optComment = commentRepository.findById(dto.getId());
        if (optComment.isPresent()) {
            commentRepository.deleteById(dto.getId());
        }
    }
}
