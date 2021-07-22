package com.example.event_project.component;

import com.example.event_project.model.ERole;
import com.example.event_project.model.Role;
import com.example.event_project.model.User;
import com.example.event_project.repository.RoleRepository;
import com.example.event_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;


    @Autowired
    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder encoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;

        System.out.println("działa");
        loadData();
    }

    private void loadData() {
        for (ERole rola : ERole.values()) {
            Role role = new Role();
            role.setName(rola);
            roleRepository.save(role);
        }

        Optional<Role> role = roleRepository.findByName(ERole.ROLE_ADMIN);
        User admin = User.builder()
                .email("admin@admin.pl")
                .showName("admin")
                .login("admin")
                .password(encoder.encode("adminadmin"))
                .roles(Set.of(role.get()))
                .build();

        userRepository.save(admin);
    }
}
