package com.example.dindonback.Login_SignUp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtTokenProviderTest {

    @InjectMocks
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationMs", 600000);  // Set expiration time to 10 minutes
    }

    @Test
    void testGenerateAndValidateToken() {
        String testEmail = "test@example.com";

        String token = jwtTokenProvider.generateToken(testEmail);
        assertNotNull(token);

        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void testGetEmailFromToken() {
        String testEmail = "test@example.com";

        String token = jwtTokenProvider.generateToken(testEmail);
        assertNotNull(token);

        String emailFromToken = jwtTokenProvider.getEmailFromToken(token);
        assertEquals(testEmail, emailFromToken);
    }

    @Test
    void testResolveToken() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer testToken");

        String resolvedToken = jwtTokenProvider.resolveToken(mockRequest);
        assertEquals("testToken", resolvedToken);
    }

    @Test
    void testResolveTokenNoBearer() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn("testToken");

        String resolvedToken = jwtTokenProvider.resolveToken(mockRequest);
        assertNull(resolvedToken);
    }
}
