package com.unagra.auth.jwt.authservice.service;

import com.unagra.auth.jwt.authservice.entities.RefreshToken;
import com.unagra.auth.jwt.authservice.repository.AuthUserRepository;
import com.unagra.auth.jwt.authservice.repository.RefreshTokenRepository;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private AuthUserRepository authUserRepository;

    public RefreshToken createRefreshToken(Long customerid) {
        RefreshToken refreshToken = RefreshToken.builder()
                .authUser(authUserRepository.findByCustomerid(customerid).get())
                //.authUser(authUserRepository .findByCustomerID(customerid))
                .refreshtoken(UUID.randomUUID().toString())
                .timesessionexpire(Instant.now().plusMillis(1200000)) // orgin toke has a 15 minute, refresh token has 20 minutes...
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String refreshToken) {
        //public RefreshToken findBytoken(String refreshToken) {
        //return refreshTokenRepository.findByRefreshtoken(refreshToken);
        return refreshTokenRepository.findByRefreshtoken(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getTimesessionexpire().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getRefreshtoken() + "El token ha expirado, es necesario iniciar sesión nuevamente.");
        }

        //if token is alive...
        return token;
    }
}
