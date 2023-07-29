package com.example.dindonback.DTOs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void getEmailTest() {
        String emailValue = "testEmail@mail.com";
        user.setEmail(emailValue);

        assertEquals(emailValue, user.getEmail());
    }

    // Repite lo anterior para los demás campos
    // ...

    @Test
    void getFriendsTest() {
        List<String> friends = Arrays.asList("friend1", "friend2", "friend3");
        user.setFriends(friends);

        assertEquals(friends, user.getFriends());
    }

    @Test
    void getFriendsRequestsTest() {
        List<String> friendRequests = Arrays.asList("request1", "request2", "request3");
        user.setFriendsRequests(friendRequests);

        assertEquals(friendRequests, user.getFriendsRequests());
    }

    @Test
    void getPreferenciasTest() {
        List<String> preferences = Arrays.asList("preference1", "preference2", "preference3");
        user.setPreferencias(preferences);

        assertEquals(preferences, user.getPreferencias());
    }

    @Test
    void constructorWithEmailAndPasswordTest() {
        User newUser = new User("testEmail@mail.com", "testPassword");

        assertEquals("testEmail@mail.com", newUser.getEmail());
        assertEquals("testPassword", newUser.getPassword());
        assertTrue(newUser.getFriends().isEmpty());
        assertTrue(newUser.getFriendsRequests().isEmpty());
        assertTrue(newUser.getPreferencias().isEmpty());
    }

    @Test
    void noArgConstructorTest() {
        User newUser = new User();

        assertTrue(newUser.getFriends().isEmpty());
        assertTrue(newUser.getFriendsRequests().isEmpty());
        assertTrue(newUser.getPreferencias().isEmpty());
    }
}
