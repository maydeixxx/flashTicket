package com.FlashTicket.GatewayService.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

import java.util.List;

@Configuration
public class GatewayConfig {

    @Bean
    public AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }

    @Bean
    public List<String> allowedPaths() {
        return List.of("/api/v1/users/auth", "/api/v1/users/auth/login");
    }

}
