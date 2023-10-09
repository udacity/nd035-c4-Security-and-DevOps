package com.example.demo.security.filter;

import com.auth0.jwt.JWT;
import com.example.demo.constant.AppConstant;
import com.example.demo.security.support.KeyUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Component
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    String authHeader = request.getHeader(AppConstant.AUTHORIZATION_HEADER);
    if (authHeaderIsNull(authHeader)) {
      chain.doFilter(request, response);
      return;
    }
    SecurityContextHolder.getContext().setAuthentication(getAuthenticationToken(authHeader));
    chain.doFilter(request, response);
  }

  private boolean authHeaderIsNull(String authHeader) {
    return authHeader == null || !authHeader.startsWith(AppConstant.BEARER_HEADER);
  }

  private UsernamePasswordAuthenticationToken getAuthenticationToken(String authHeader) {
    String token = authHeader.replace(AppConstant.BEARER_HEADER, "");
    // Todo: Replace with RSA512 and public, private key
    String user = JWT.require(HMAC512(KeyUtil.getSecretKey())).build()
        .verify(token)
        .getSubject();
    if (user != null) {
      return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
    }
    return null;
  }
}
