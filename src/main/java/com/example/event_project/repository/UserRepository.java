package com.example.event_project.repository;

import com.example.event_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // todo : nieużywane metody
    Optional<User> findByLoginAndPassword(String login, String password);

    Optional<User> findByLoginOrEmailOrShowName(String login, String email, String showName);

    Optional<User> findByLogin(String username);

    Optional<User> findByEmail(String email);

    Boolean existsByShowName(String showname);

    Boolean existsByLogin(String login);

    Boolean existsByEmail(String email);

}
