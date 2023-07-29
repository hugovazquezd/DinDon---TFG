package com.example.dindonback.Login_SignUp;

import com.example.dindonback.DTOs.*;
import com.example.dindonback.bbdd.RoleRepository;
import com.example.dindonback.bbdd.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    RoleRepository roleRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticateUser() {
        String testEmail = "test@example.com";
        String testPassword = "password";

        User testUser = new User();
        testUser.setEmail(testEmail);
        testUser.setPassword(testPassword);

        when(userRepository.findByEmail(testEmail)).thenReturn(testUser);
        when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtTokenProvider.generateToken(any())).thenReturn("testToken");

        ResponseEntity<?> response = authController.authenticateUser(new LoginRequest(testEmail, testPassword));

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        // Add more assertions based on your requirements
    }

    @Test
    void registerUser() {
        String testEmail = "test@example.com";
        String testPassword = "password";

        when(userRepository.existsByEmail(testEmail)).thenReturn(false);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(java.util.Optional.of(new Role()));

        ResponseEntity<?> response = authController.registerUser(new SignUpRequest(testEmail, testPassword));

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        // Add more assertions based on your requirements
    }

    @Test
    void authenticateUserNotFound() {
        String testEmail = "test@example.com";
        String testPassword = "password";

        when(userRepository.findByEmail(testEmail)).thenReturn(null);

        ResponseEntity<?> response = authController.authenticateUser(new LoginRequest(testEmail, testPassword));

        assertEquals(401, response.getStatusCodeValue());
    }


    @Test
    void authenticateUserWrongPassword() {
        String testEmail = "test@example.com";
        String testPassword = "password";

        User testUser = new User();
        testUser.setEmail(testEmail);
        testUser.setPassword(testPassword);

        when(userRepository.findByEmail(testEmail)).thenReturn(testUser);
        when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(false);

        ResponseEntity<?> response = authController.authenticateUser(new LoginRequest(testEmail, testPassword));

        assertEquals(401, response.getStatusCodeValue());
    }

}
