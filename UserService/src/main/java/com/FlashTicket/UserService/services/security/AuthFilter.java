package com.FlashTicket.UserService.services.security;

import com.FlashTicket.UserService.services.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class AuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization != null && !authorization.isBlank() && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            Claims claims = jwtService.parseClaims(token);

            try {
                if (!token.isBlank()) {
                    String email = claims.getSubject();
                    String rawRole = claims.get("Role").toString();
                    String role = rawRole.startsWith("ROLE_") ? rawRole : "ROLE_" + rawRole;
                    UserSecurity userSecurity = new UserSecurity(email, role);
                    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userSecurity,
                                null,
                                authorities
                        );
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            } catch (Exception e) {
                log.error("Error filtering request: {}", e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}
