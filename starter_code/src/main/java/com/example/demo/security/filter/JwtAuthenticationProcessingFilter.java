package com.example.demo.security.filter;

import com.auth0.jwt.JWT;
import com.example.demo.constant.AppConstant;
import com.example.demo.model.requests.LoginRequest;
import com.example.demo.security.support.KeyUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JwtAuthenticationProcessingFilter extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager authManager;

  public JwtAuthenticationProcessingFilter(AuthenticationManager authenticationManager) {
    this.authManager = authenticationManager;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
      LoginRequest credentials = new ObjectMapper()
          .readValue(request.getInputStream(), LoginRequest.class);

      return authManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              credentials.getUsername(),
              credentials.getPassword(),
              new ArrayList<>()));
    } catch (IOException e) {
      throw new BadCredentialsException(e.getMessage(), e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    String token = JWT.create()
        .withSubject(((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername())
        .withExpiresAt(Instant.now().plusMillis(AppConstant.TOKEN_EXPIRATION_TIME))
        .sign(HMAC512(KeyUtil.getSecretKey().getBytes()));
    response.addHeader(AppConstant.AUTHORIZATION_HEADER, AppConstant.BEARER_HEADER + token);
  }
}
