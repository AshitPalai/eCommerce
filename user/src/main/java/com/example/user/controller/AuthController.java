package com.example.user.controller;


import com.example.user.dto.JwtResponse;
import com.example.user.dto.LoginRequest;
import com.example.user.dto.LogoutRequest;
import com.example.user.dto.TokenRefreshRequest;
import com.example.user.entity.RefreshToken;
import com.example.user.repository.UserRepository;
import com.example.user.security.JwtUtil;
import com.example.user.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String accessToken = jwtUtil.generateToken(request.getEmail());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.getEmail());

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken.getToken()
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {

        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtil.generateToken(user.getEmail());

                    return ResponseEntity.ok(new JwtResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest request) {

        refreshTokenService.deleteByToken(request.getRefreshToken());

        return ResponseEntity.ok("Logged out successfully");
    }
}


