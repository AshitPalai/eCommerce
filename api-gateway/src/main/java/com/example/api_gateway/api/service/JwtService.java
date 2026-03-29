package com.example.api_gateway.api.service;

public interface JwtService {


    String extractUsername(String token);

    String extractRole(String token);

    boolean isTokenValid(String token);
}
