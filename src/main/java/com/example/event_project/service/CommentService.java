package com.example.event_project.service;

import com.example.event_project.exceptions.EventNotFindException;
import com.example.event_project.exceptions.UserNotFindException;
import com.example.event_project.model.Comment;
import com.example.event_project.model.Event;
import com.example.event_project.model.User;
import com.example.event_project.model.dto.CommentDto;
import com.example.event_project.model.dto.mapper.CommentMapper;
import com.example.event_project.repository.CommentRepository;
import com.example.event_project.repository.EventRepository;
import com.example.event_project.repository.UserRepository;
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
    private final UserRepository userRepository;


    public List<CommentDto> getComments(Long id) {
        return commentRepository.findCommentByEvent_IdOrderByPostTime(id)
                .stream()
                .map(commentMapper::commentToDto)
                .collect(Collectors.toList());
    }

    public CommentDto addComment(Long eventId, String userName, CommentDto dto) throws UserNotFindException, EventNotFindException {
        Optional<User> optUser = userRepository.findByLogin(userName);
        if (optUser.isEmpty()) {
            throw new UserNotFindException();
        }
        Optional<Event> optEvent = eventRepository.findById(eventId);
        if (optEvent.isEmpty()) {
            throw new EventNotFindException();
        }
        Comment comment = commentMapper.dtoToComment(dto);
        comment.setEvent(optEvent.get());
        comment.setUser(optUser.get());
        comment.setPostTime(LocalDateTime.now());
        comment = commentRepository.save(comment);
        return commentMapper.commentToDto(comment);
    }

    public void deleteComment(CommentDto dto) {
        Optional<Comment> optComment = commentRepository.findById(dto.getId());
        if (optComment.isPresent()) {
            commentRepository.deleteById(dto.getId());
        }
    }
}
