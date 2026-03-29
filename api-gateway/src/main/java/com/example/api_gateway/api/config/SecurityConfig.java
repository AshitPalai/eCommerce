package com.example.api_gateway.api.config;

import com.example.api_gateway.api.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchange -> exchange

                        // Public
                        .pathMatchers("/api/v1/auth/**").permitAll()

                        // Admin only
                        .pathMatchers(HttpMethod.POST, "/api/v1/categories").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/categories/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/api/v1/categories/**").hasRole("ADMIN")

                        // Others
                        .anyExchange().authenticated()
                )
                .build();
    }
}
