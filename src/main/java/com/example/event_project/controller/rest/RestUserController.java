package com.example.event_project.controller.rest;

import com.example.event_project.exceptions.UserNotFindException;
import com.example.event_project.model.dto.UserDto;
import com.example.event_project.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/Users")
@RequiredArgsConstructor
public class RestUserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getList() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("{Id}")
    public ResponseEntity<?> getUser(@PathVariable Long Id) {
        try {
            return ResponseEntity.ok(userService.findById(Id));
        } catch (UserNotFindException e) {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @PatchMapping()
    public ResponseEntity<?> updateRoles(@RequestBody UserDto dto) {
        try {
            userService.updateUser(dto);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .build();
        } catch (UserNotFindException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @DeleteMapping("{Id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long Id) {
        try {
            userService.deleteUser(Id);
            return ResponseEntity.ok()
                    .build();
        } catch (UserNotFindException e) {
            return ResponseEntity.notFound()
                    .build();
        }
    }
}
