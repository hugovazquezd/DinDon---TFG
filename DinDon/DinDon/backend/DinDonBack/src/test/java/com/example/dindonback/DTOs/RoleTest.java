package com.example.dindonback.DTOs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleTest {
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
    }

    @Test
    void getIdTest() {
        Long idValue = 1L;
        role.setId(idValue);

        assertEquals(idValue, role.getId());
    }

    @Test
    void getNameTest() {
        ERole roleName = ERole.ROLE_USER;
        role.setName(roleName);

        assertEquals(roleName, role.getName());
    }

    @Test
    void setNameTest() {
        ERole roleName = ERole.ROLE_ADMIN;
        role.setName(roleName);

        assertEquals(roleName, role.getName());
    }

    @Test
    void constructorTest() {
        ERole roleName = ERole.ROLE_USER;
        Role newRole = new Role(roleName);

        assertEquals(roleName, newRole.getName());
    }
}
