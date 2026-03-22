package com.example.user.service;

import com.example.user.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(String email);

    RefreshToken verifyExpiration(RefreshToken token);

    Optional<RefreshToken> findByToken(String token);

    void deleteByUserEmail(String email);

    void deleteByToken(String token);
}
