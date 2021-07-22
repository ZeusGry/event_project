package com.example.event_project.component;

import com.example.event_project.model.ERole;
import com.example.event_project.model.Role;
import com.example.event_project.model.User;
import com.example.event_project.repository.RoleRepository;
import com.example.event_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        System.out.println("działa");
        //loadData();
    }

    private void loadData() {
        for (ERole rola : ERole.values()) {
            Role role = new Role();
            role.setName(rola);
            roleRepository.save(role);
        }

        Role role = new Role();
        role.setName(ERole.ROLE_USER);
        User admin = User.builder()
                .email("admin@admin.pl")
                .showName("admin")
                .login("admin")
                .password("adminadmin")
                .roles(Set.of(role))
                .build();

        userRepository.save(admin);
    }
}
