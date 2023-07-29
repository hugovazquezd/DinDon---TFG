package com.example.dindonback.bbdd;

import com.example.dindonback.DTOs.ERole;
import com.example.dindonback.DTOs.Role;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
public class DataInitializer {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initRoles() {
        if (roleRepository.count() == 0) {
            Arrays.stream(ERole.values())
                    .map(Role::new)
                    .forEach(roleRepository::save);
        }
    }
}
