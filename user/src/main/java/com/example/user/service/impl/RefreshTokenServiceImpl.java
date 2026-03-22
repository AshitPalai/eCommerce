package com.example.user.service.impl;

import com.example.user.entity.RefreshToken;
import com.example.user.entity.User;
import com.example.user.repository.RefreshTokenRepository;
import com.example.user.repository.UserRepository;
import com.example.user.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    private final long REFRESH_TOKEN_DURATION = 7 * 24 * 60 * 60; // 7 days

    @Override
    public RefreshToken createRefreshToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RefreshToken token = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusSeconds(REFRESH_TOKEN_DURATION))
                .build();

        return refreshTokenRepository.save(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired");
        }

        return token;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return Optional.ofNullable(refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token not found")));
    }

    @Override
    public void deleteByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        refreshTokenRepository.deleteByUser(user);
    }

    @Override
    public void deleteByToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        refreshTokenRepository.delete(refreshToken);
    }


}

