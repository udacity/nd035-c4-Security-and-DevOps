package com.example.demo.security;

import com.example.demo.model.persistence.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.FilterChain;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class JWTAuthenticationFilterTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private final ObjectMapper objectMapper = mock(ObjectMapper.class);
    private final JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(authenticationManager, objectMapper);
    private final Authentication authentication = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD, Collections
            .emptyList());
    private final HttpServletRequest req = mock(HttpServletRequest.class);
    private final HttpServletResponse res = mock(HttpServletResponse.class);
    private final FilterChain chain = mock(FilterChain.class);

    @Test
    public void attemptAuthenticationReturnsExpectedAuthenticationForGivenObject() throws IOException {
        final User user = new User(USERNAME, PASSWORD);
        when(req.getInputStream()).thenReturn(mock(ServletInputStream.class));
        when(authenticationManager.authenticate(authentication)).thenReturn(authentication);
        when(objectMapper.readValue(any(InputStream.class), eq(User.class))).thenReturn(user);

        final Authentication authRes = jwtAuthenticationFilter.attemptAuthentication(req, res);

        assertEquals(authRes, authentication);
    }

    @Test(expected = RuntimeException.class)
    public void attemptAuthenticationReturnsRuntimeExceptionInCaseOfIOException() throws IOException {
        when(req.getInputStream()).thenThrow(IOException.class);

        jwtAuthenticationFilter.attemptAuthentication(req, res);
    }

    @Test
    public void successfulAuthenticationAddsExpectedHeader() {
        final Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(new org.springframework.security.core.userdetails.User(USERNAME, PASSWORD, Collections
                .emptyList()));

        jwtAuthenticationFilter.successfulAuthentication(req, res, chain, auth);

        verify(res).addHeader(eq(SecurityConstants.HEADER_STRING), startsWith(SecurityConstants.TOKEN_PREFIX));
    }

}
