package com.example.event_project.service;

import com.example.event_project.exceptions.UserNotFindException;
import com.example.event_project.model.Organizer;
import com.example.event_project.model.Participant;
import com.example.event_project.model.Role;
import com.example.event_project.model.User;
import com.example.event_project.model.dto.UserDto;
import com.example.event_project.model.dto.mapper.UserMapper;
import com.example.event_project.repository.OrganizerRepository;
import com.example.event_project.repository.ParticipantRepository;
import com.example.event_project.repository.RoleRepository;
import com.example.event_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final OrganizerRepository organizerRepository;
    private final ParticipantRepository participantRepository;

    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::userToDto)
                .collect(Collectors.toList());
    }

    public UserDto findById(Long id) throws UserNotFindException {
        Optional<User> optUser = userRepository.findById(id);
        if (optUser.isPresent()) {
            return userMapper.userToDto(optUser.orElse(new User()));
        }
        throw new UserNotFindException();
    }

    public void updateUser(UserDto dto) throws UserNotFindException {
        Optional<User> optUser = userRepository.findById(dto.getId());

        if (optUser.isPresent()) {
            Set<Role> newListOfRoles = new HashSet<>();
            for (Role role : dto.getRoles()) {
                Optional<Role> tempRole = roleRepository.findByName(role.getName());
                tempRole.ifPresent(newListOfRoles::add);
            }
            optUser.get()
                    .setRoles(newListOfRoles);
            userRepository.save(optUser.get());
            return;
        }
        throw new UserNotFindException();
    }

    public void deleteUser(Long id) throws UserNotFindException {
        Optional<User> optUser = userRepository.findById(id);
        if (optUser.isPresent()) {
            List<Participant> participantsToRemove = participantRepository.findByUserId(optUser.get()
                    .getId());
            participantRepository.deleteAll(participantsToRemove);
            List<Organizer> organizersToRemove = organizerRepository.findByUserId(optUser.get()
                    .getId());
            organizerRepository.deleteAll(organizersToRemove);
            userRepository.deleteById(id);
            return;
        }
        throw new UserNotFindException();
    }
}
