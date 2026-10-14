package com.FlashTicket.GatewayService.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter implements Ordered, GlobalFilter {
    private final JwtService jwtService;
    private final List<String> allowedPaths;
    private final AntPathMatcher antPathMatcher;

    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull GatewayFilterChain chain) {
        try {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpRequest mutatedRequest = request.mutate().headers(httpHeaders -> {
                httpHeaders.remove("X-User-Id");
                httpHeaders.remove("X-User-Email");
                httpHeaders.remove("X-User-Role");
            }).build();


            if (HttpMethod.OPTIONS.equals(request.getMethod())) {
                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            }

            String requestPath = request.getURI().getPath();

            for (String pattern : allowedPaths) {
                if (antPathMatcher.match(pattern, requestPath)) {
                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                }
            }

            String authorizationHeader;
            String jwtToken;

            if (request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION) != null) {
                authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            } else {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            if (authorizationHeader != null && !authorizationHeader.isBlank() && authorizationHeader.startsWith("Bearer ")) {
                jwtToken = authorizationHeader.substring(7);
            } else {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            Claims claimsFromToken = jwtService.getClaimsFromToken(jwtToken);
            String email = claimsFromToken.getSubject();
            Object userId = claimsFromToken.get("User_id");
            Object rawRole = claimsFromToken.get("Role");

            if (email == null || email.isBlank() || userId == null || rawRole == null) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String role = rawRole.toString().startsWith("ROLE_") ? rawRole.toString() : "ROLE_" + rawRole;
            ServerHttpRequest requestWithHeaders = exchange.getRequest().mutate().headers(headers -> {
                headers.set("X-User-Id", userId.toString());
                headers.set("X-User-Role", role);
                headers.set("X-User-Email", email);
            }).build();

            return chain.filter(exchange.mutate().request(requestWithHeaders).build());
        } catch (MalformedJwtException | SignatureException e) {
            log.warn("Token malformed: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        } catch (ExpiredJwtException e) {
            log.warn("Token expired: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        } catch (Exception e) {
            log.error("Error filtering request");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
