package com.example.event_project.controller.rest;

import com.example.event_project.model.dto.payload.request.LoginRequest;
import com.example.event_project.model.dto.payload.request.SingupRequest;
import com.example.event_project.model.dto.payload.response.JwtResponse;
import com.example.event_project.model.dto.payload.response.MessageResponse;
import com.example.event_project.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Base64;


@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @GetMapping("/{token}")
    public ResponseEntity<?> getEmail(@PathVariable String token) {
        System.out.println(token);
        byte[] decodedBytes = Base64.getDecoder()
                .decode(token);
        String decodedString = new String(decodedBytes);
        System.out.println(token);
        return ResponseEntity.ok(decodedString);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SingupRequest signUpRequest) {

        MessageResponse messageResponse = authService.registerUser(signUpRequest);
        if (messageResponse.getMessage()
                .startsWith("Error")) {
            return ResponseEntity
                    .badRequest()
                    .body(messageResponse);
        }
        return ResponseEntity.ok(messageResponse);
    }


}
