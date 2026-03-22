package com.example.user.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {


    private final String SECRET_KEY = "mysecretkey123456789mysecretkey123456"; // ✅ >= 32 chars

    // ✅ Always use same key method
    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String username) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 30000))
                .signWith(getKey()) // ✅ correct
                .compact();
    }

    public String extractUsername(String token) {

        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getKey()) // ✅ FIXED
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
