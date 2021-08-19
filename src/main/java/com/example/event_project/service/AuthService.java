package com.example.event_project.service;

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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final OrganizerToAddRepository organizerToAddRepository;
    private final OrganizerRepository organizerRepository;


    public JwtResponse authenticateUser(LoginRequest loginRequest) {

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

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
    }

    public MessageResponse registerUser(SingupRequest signUpRequest) {
        if (userRepository.existsByLogin(signUpRequest.getUsername())) {
            new MessageResponse("Error: Login is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            new MessageResponse("Error: Email is already in use!");
        }

        if (userRepository.existsByShowName(signUpRequest.getShowName())) {
            return new MessageResponse("Error: ShowName is already in use!");
        }

        User user = createNewUser(signUpRequest);

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            chooseRole(strRoles, roles);
        }

        user.setRoles(roles);
        User savedUser = userRepository.save(user);
        List<OrganizerToAdd> organizersToAdd = organizerToAddRepository.findAllByUserEmail(user.getEmail());
        List<Event> eventsList = organizersToAdd
                .stream()
                .map(OrganizerToAdd::getEvent)
                .collect(Collectors.toList());
        for (Event event : eventsList) {
            organizerRepository.save(new Organizer(savedUser, event));
            organizerToAddRepository.deleteAll(organizersToAdd);
        }
        return new MessageResponse("User registered successfully!");
    }

    private void chooseRole(Set<String> strRoles, Set<Role> roles) {
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

    private User createNewUser(SingupRequest signUpRequest) {
        return new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getShowName(),
                encoder.encode(signUpRequest.getPassword()));
    }
}
