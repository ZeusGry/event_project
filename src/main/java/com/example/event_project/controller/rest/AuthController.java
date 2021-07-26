package com.example.event_project.controller.rest;

import com.example.event_project.configuration.security.jwt.JwtUtils;
import com.example.event_project.model.*;
import com.example.event_project.model.dto.payload.request.LoginRequest;
import com.example.event_project.model.dto.payload.request.SingupRequest;
import com.example.event_project.model.dto.payload.response.JwtResponse;
import com.example.event_project.model.dto.payload.response.MessageResponse;
import com.example.event_project.repository.OrganizerRepository;
import com.example.event_project.repository.OrganizerToAddRepository;
import com.example.event_project.repository.RoleRepository;
import com.example.event_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final RoleRepository roleRepository;
    private final OrganizerToAddRepository organizerToAddRepository;
    private final OrganizerRepository organizerRepository;

    // TODO: spróbować przenieść impl. do serwisu
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
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
        // todo: przenieść więcej odpowiedzialności na serwisy
        //  kontroler powinien być na tyle czysty żeby łatwo było odczytać scieżkę oraz
        //  możliwe komunikaty/odpowiedzi
        if (userRepository.existsByLogin(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Login is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        if (userRepository.existsByShowName(signUpRequest.getShowName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: ShowName is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getShowName(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        System.out.println(strRoles);
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        User savedUser = userRepository.save(user);
        List<Event> eventsList = organizerToAddRepository.findAllByUserEmail(user.getEmail())
                .stream()
                .map(OrganizerToAdd::getEvent)
                .collect(Collectors.toList());
        for (Event event : eventsList) {
            organizerRepository.save(new Organizer(savedUser, event));
            organizerToAddRepository.deleteByUserEmail(user.getEmail());
        }
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


}
