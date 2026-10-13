package com.FlashTicket.UserService.services;

import com.FlashTicket.UserService.models.domain.enums.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey secretKey;

    @Value("${jwt.token.lifetime}")
    private Duration tokenLifeTime;

    public JwtService(@Value("${jwt.secret.key}") String secretValue) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretValue));
    }

    public String generateToken(String email, Role role) {
        return Jwts.builder()
                .subject(email)
                .claim("Role", role)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(tokenLifeTime)))
                .signWith(secretKey)
                .compact();
    }
}
