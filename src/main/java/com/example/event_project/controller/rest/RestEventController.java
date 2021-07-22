package com.example.event_project.controller.rest;

import com.example.event_project.configuration.security.jwt.JwtUtils;
import com.example.event_project.exceptions.EventNotFindException;
import com.example.event_project.exceptions.UserNotFindException;
import com.example.event_project.model.dto.EventDto;
import com.example.event_project.model.dto.PresentDto;
import com.example.event_project.model.dto.payload.response.MessageResponse;
import com.example.event_project.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.mail.MessagingException;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("api/Events")
@RequiredArgsConstructor
public class RestEventController {

    private final EventService eventService;
    private final JwtUtils jwtUtils;


    @GetMapping
    public ResponseEntity<?> getList() {
        return ResponseEntity.ok(eventService.getListOfEventsDto());
    }

    @GetMapping("/accepted/{id}")
    public ResponseEntity<?> getListOfUserEvents(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getListOfEventsDtoByUser(id));
    }

    @GetMapping("/organizer/{id}")
    public ResponseEntity<?> getListOrganized(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getListOfEventsDtoOrganized(id));
    }

    @GetMapping("{Id}")
    public ResponseEntity<?> get(@PathVariable Long Id) {
        return ResponseEntity.ok(eventService.getEventDto(Id));
    }

    @GetMapping("/isParticipiant/{id}")
    public ResponseEntity<?> isParticipiant(@PathVariable Long id, @RequestHeader(name = "Authorization") String token) {
        String name = jwtUtils.getUserNameFromJwtToken(token.substring(7));
        System.out.println(name);
        try {
            PresentDto present = new PresentDto(eventService.isParticipiant(id, name));
            System.out.println(present.isPresent());
            return ResponseEntity.ok(present);
        } catch (UserNotFindException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Użytkownik nie istnieje"));
        }
    }

    @PostMapping("/setOrg/{id}")
    public ResponseEntity<?> setOrganizators(@PathVariable Long id, @RequestBody String[] organizators, @RequestHeader(name = "Authorization") String token) {
        String name = jwtUtils.getUserNameFromJwtToken(token.substring(7));
        try {
            eventService.setOrganizators(id, name, organizators);
            return ResponseEntity.ok()
                    .build();
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .build();
        }
    }


    @PostMapping()
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public ResponseEntity<?> add(@RequestBody EventDto eventDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventService.addEvent(eventDto));
    }

    @PatchMapping("/join")
    @CrossOrigin
    // @RolesAllowed({ "ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR" })
    public ResponseEntity<?> joinEvent(@RequestParam Long userId, @RequestParam Long eventId) {
        try {
            eventService.joinEvent(userId, eventId);
            return ResponseEntity.ok()
                    .build();
        } catch (EventNotFindException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Event nie istnieje"));
        } catch (UserNotFindException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Użytkownik nie istnieje"));
        }
    }

    @DeleteMapping("/join")
    @CrossOrigin
    // @RolesAllowed({ "ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR" })
    public ResponseEntity<?> quitEvent(@RequestParam Long userId, @RequestParam Long eventId) {
        try {
            eventService.quitEvent(userId, eventId);
            return ResponseEntity.ok()
                    .build();
        } catch (EventNotFindException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Event nie istnieje"));
        } catch (UserNotFindException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Użytkownik nie istnieje"));
        }
    }

    @DeleteMapping()
    public ResponseEntity<?> delete(@RequestBody EventDto dto) {
        if (eventService.deleteEvent(dto)) {
            return ResponseEntity.ok()
                    .build();
        }
        return ResponseEntity.notFound()
                .build();
    }
}
