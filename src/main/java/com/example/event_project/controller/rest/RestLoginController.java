package com.example.event_project.controller.rest;

import com.example.event_project.model.User;
import com.example.event_project.model.dto.UserDto;
import com.example.event_project.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class RestLoginController {
    private final UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody UserDto user) {
        Optional<User> optUser = userService.checkUser(user);
        if (optUser.isPresent()) {
            return Jwts.builder()
                    .setSubject(user.getLogin())
                    .claim("role", optUser.get()
                            .getRole()
                            .toString())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 3600_000))
                    .signWith(SignatureAlgorithm.HS512, user.getPassword())
                    .compact();
        }
        return null;
    }

}
