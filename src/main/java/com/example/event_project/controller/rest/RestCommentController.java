package com.example.event_project.controller.rest;

import com.example.event_project.model.dto.CommentDto;
import com.example.event_project.service.CommentService;
import com.example.event_project.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/Comments")
@RequiredArgsConstructor
public class RestCommentController {
    private final EventService eventService;
    private final CommentService commentService;

    @GetMapping("{eventId}")
    public List<CommentDto> getCommentsForEvent(@PathVariable Long eventId) {
        return commentService.getComments(eventId);
    }

    @PostMapping("{eventId}")
    public CommentDto add(@PathVariable Long eventId, @RequestBody CommentDto dto) {
        // TODO
        // Trzeba zrobić warstwę seciurity, by móc zapisać wraz z userem, bo sypie błędami

        return commentService.addComment(eventId, dto);
    }
//             TODO
//            @PatchMapping()
//            public CommentDto update(@RequestBody CommentDto dto) {
//                return ;
//            }

    @DeleteMapping()
    public void delete(@RequestBody CommentDto dto) {
        commentService.deleteComment(dto);
    }
}
