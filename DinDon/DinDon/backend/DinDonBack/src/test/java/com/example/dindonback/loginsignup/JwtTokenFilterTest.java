package com.example.dindonback.loginsignup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtTokenFilterTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtTokenFilter jwtTokenFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtTokenFilter = new JwtTokenFilter(jwtTokenProvider);
    }

    @Test
    void testDoFilterWithValidToken() throws Exception {
        String testEmail = "test@example.com";
        String testToken = "validToken";
        when(jwtTokenProvider.resolveToken(request)).thenReturn(testToken);
        when(jwtTokenProvider.validateToken(testToken)).thenReturn(true);
        when(jwtTokenProvider.getEmailFromToken(testToken)).thenReturn(testEmail);

        jwtTokenFilter.doFilter(request, response, filterChain);

        UsernamePasswordAuthenticationToken expectedAuth =
                new UsernamePasswordAuthenticationToken(testEmail, null, null);
        assertEquals(expectedAuth, SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterWithInvalidToken() throws Exception {
        String testToken = "invalidToken";
        when(jwtTokenProvider.resolveToken(request)).thenReturn(testToken);
        when(jwtTokenProvider.validateToken(testToken)).thenReturn(false);

        jwtTokenFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterWithNoToken() throws Exception {
        when(jwtTokenProvider.resolveToken(request)).thenReturn(null);

        jwtTokenFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}
