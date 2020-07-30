package com.elsevier.elsevierauthservice.security;

import com.elsevier.elsevierauthservice.service.impl.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
  private static final String AUTH_HEADER = "Authorization";
  private static final String BEARER = "Bearer ";

  @Autowired private JwtTokenProvider jwtTokenProvider;

  @Autowired private UserDetailsServiceImpl customUserDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String jwt = getJwtFromRequest(request);

      if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
        UUID userId = jwtTokenProvider.getUserIdFromJwt(jwt);
        UserDetails userDetails = customUserDetailsService.loadUserById(userId);

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception ex) {
      logger.error("Could not set user authentication in security context", ex);
    }

    filterChain.doFilter(request, response);
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader(JwtAuthenticationFilter.AUTH_HEADER);

    if (StringUtils.hasText(bearerToken)
        && bearerToken.startsWith(JwtAuthenticationFilter.BEARER)) {
      return bearerToken.substring(7);
    }

    return null;
  }
}
