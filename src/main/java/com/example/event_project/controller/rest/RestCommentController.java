package com.example.event_project.controller.rest;

import com.example.event_project.configuration.security.jwt.JwtUtils;
import com.example.event_project.exceptions.EventNotFindException;
import com.example.event_project.exceptions.UserNotFindException;
import com.example.event_project.model.dto.CommentDto;
import com.example.event_project.service.CommentService;
import com.example.event_project.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/Comments")
@RequiredArgsConstructor
public class RestCommentController {
    private final CommentService commentService;
    private final JwtUtils jwtUtils;



    @GetMapping("{eventId}")
    @RolesAllowed({ "ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR" })
    public ResponseEntity<List<CommentDto>> getCommentsForEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(commentService.getComments(eventId));
    }

    @PostMapping("{eventId}")
    @RolesAllowed({ "ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR" })
    public ResponseEntity<CommentDto> add(@PathVariable Long eventId, @RequestHeader(name = "Authorization") String token, @RequestBody CommentDto dto) {
        String name = jwtUtils.getUserNameFromJwtToken(token.substring(7));
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(commentService.addComment(eventId, name, dto));
        } catch (UserNotFindException | EventNotFindException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }


    @DeleteMapping()
    @RolesAllowed({ "ROLE_ADMIN" })
    public void delete(@RequestBody CommentDto dto) {
        commentService.deleteComment(dto);
    }
}
