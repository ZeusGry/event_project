package com.example.event_project.repository;

import com.example.event_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginAndPassword(String login, String password);

    Optional<User> findByLoginOrEmailOrShowName(String login, String email, String showName);
}