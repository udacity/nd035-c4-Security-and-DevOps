package com.example.demo.security;

import com.auth0.jwt.JWT;
import org.junit.Test;
import org.springframework.security.authentication.AuthenticationManager;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static org.mockito.Mockito.*;

public class JWTAuthenticationVerficationFilterTest {

    private static final String VALID_TOKEN = JWT.create()
            .withSubject("username")
            .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
            .sign(HMAC512(SecurityConstants.SECRET.getBytes()));
    private static final String TOKEN_WITHOUT_SUBJECT = JWT.create()
            .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
            .sign(HMAC512(SecurityConstants.SECRET.getBytes()));
    private final HttpServletRequest req = mock(HttpServletRequest.class);
    private final HttpServletResponse res = mock(HttpServletResponse.class);
    private final FilterChain chain = mock(FilterChain.class);
    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private final JWTAuthenticationVerficationFilter filter = new JWTAuthenticationVerficationFilter(authenticationManager);

    @Test
    public void doFilterInternalCalldoFilterWhenHeaderNull() throws IOException, ServletException {
        filter.doFilterInternal(req, res, chain);

        verify(chain).doFilter(req, res);
    }

    @Test
    public void doFilterInternalCalldoFilterWhenUnexpectedHeaderPrefix() throws IOException, ServletException {
        when(req.getHeader(SecurityConstants.HEADER_STRING)).thenReturn("unexpected");

        filter.doFilterInternal(req, res, chain);

        verify(chain).doFilter(req, res);
    }

    @Test
    public void doFilterInternalCalldoFilterWhenExpectedHeader() throws IOException, ServletException {
        when(req.getHeader(SecurityConstants.HEADER_STRING)).thenReturn(SecurityConstants.TOKEN_PREFIX + VALID_TOKEN);

        filter.doFilterInternal(req, res, chain);

        verify(chain).doFilter(req, res);
    }

    @Test
    public void doFilterInternalCalldoFilterWhenUserNull() throws IOException, ServletException {
        when(req.getHeader(SecurityConstants.HEADER_STRING)).thenReturn(SecurityConstants.TOKEN_PREFIX + TOKEN_WITHOUT_SUBJECT);

        filter.doFilterInternal(req, res, chain);

        verify(chain).doFilter(req, res);
    }

}
