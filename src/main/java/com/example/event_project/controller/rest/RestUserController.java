package com.example.event_project.controller.rest;

import com.example.event_project.model.dto.UserDto;
import com.example.event_project.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/Users")
@RequiredArgsConstructor
public class RestUserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getList() {
        return userService.getUsers();
    }

    @GetMapping("{Id}")
    public UserDto get(@PathVariable Long Id) {
        return userService.findById(Id);
    }

    @PostMapping()
    public void add(@RequestBody UserDto dto) {
        userService.registerUser(dto);
    }

    @PatchMapping()
    public void update(@RequestBody UserDto dto) {
        userService.updateUser(dto);
    }

    @DeleteMapping()
    public void delete(@PathVariable Long Id) {
        userService.deleteUser(Id);
    }
}
