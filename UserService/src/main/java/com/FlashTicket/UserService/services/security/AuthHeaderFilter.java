package com.FlashTicket.UserService.services.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class AuthHeaderFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String headerId = request.getHeader("X-User-Id");
        String headerRole = request.getHeader("X-User-Role");
        String headerEmail = request.getHeader("X-User-Email");

        if (headerId != null && !headerId.isBlank() && headerRole != null && !headerRole.isBlank() && headerEmail != null && !headerEmail.isBlank()) {
            UserSecurity userSecurity = new UserSecurity(
                    headerEmail,
                    headerRole,
                    headerId
            );

            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(headerRole));

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userSecurity,
                        null,
                        authorities
                );
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }

        filterChain.doFilter(request, response);
    }
}
