package com.unagra.auth.jwt.authservice.dto;

import com.unagra.auth.jwt.authservice.entities.AuthUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenRequestDTO {
    private String refreshToken;
}
