package com.example.event_project.service;

import com.example.event_project.model.User;
import com.example.event_project.model.dto.UserDto;
import com.example.event_project.model.dto.mapper.UserMapper;
import com.example.event_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Optional<User> checkUser(UserDto user) {
        return userRepository.findByLoginAndPassword(user.getLogin(), user.getPassword());
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> userMapper.userToDto(user))
                .collect(Collectors.toList());
    }

    public UserDto findById(Long id) {
        Optional<User> optUser = userRepository.findById(id);
        return userMapper.userToDto(optUser.orElse(new User()));
    }

    public void registerUser(UserDto dto) {
        Optional<User> optUser = userRepository.findByLoginOrEmailOrShowName(dto.getLogin(), dto.getEmail(), dto.getShowName());
        if (optUser.isEmpty()) {
            User user = userMapper.dtoToUser(dto);
            userRepository.save(user);
        }
        throw new RuntimeException("Użytkownik już istnieje");
    }

    public void updateUser(UserDto dto) {
        Optional<User> optUser = userRepository.findById(dto.getId());
        if (optUser.isPresent()) {
            optUser.get()
                    .setShowName(dto.getShowName());
            userRepository.save(optUser.get());
        }
    }

    public void deleteUser(UserDto dto) {
        Optional<User> optUser = userRepository.findById(dto.getId());
        if (optUser.isPresent()) {
            userRepository.deleteById(dto.getId());
        }
    }
}
