package com.FlashTicket.InventoryService.services.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class AuthHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String rawRole = request.getHeader("X-User-Role");
        String email = request.getHeader("X-User-Email");
        String userId = request.getHeader("X-User-Id");

        if (rawRole != null && !rawRole.isBlank() && email != null && !email.isBlank() && userId != null && !userId.isBlank()) {
            String role = rawRole.startsWith("ROLE_") ? rawRole : "ROLE_" + rawRole;
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        new UserSecurity(
                            UUID.fromString(userId),
                            email
                        ),
                        null,
                        authorities
                );
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }

        filterChain.doFilter(request, response);
    }

}
