package com.unagra.auth.jwt.authservice.controller;

import com.unagra.auth.jwt.authservice.dto.AddUserDTO;
import com.unagra.auth.jwt.authservice.dto.JwtResponseDTO;
import com.unagra.auth.jwt.authservice.dto.RefreshTokenRequestDTO;
import com.unagra.auth.jwt.authservice.dto.TokenDTO;
import com.unagra.auth.jwt.authservice.entities.AuthUser;
import com.unagra.auth.jwt.authservice.entities.RefreshToken;
import com.unagra.auth.jwt.authservice.security.JwtProvider;
import com.unagra.auth.jwt.authservice.service.AuthService;
import com.unagra.auth.jwt.authservice.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/login")
    public JwtResponseDTO login(@RequestBody AddUserDTO addUserDTO) {
        TokenDTO tokenDTO = authService.login(addUserDTO);

        //if return null...
        if (tokenDTO == null) {
            throw new BadRequestException("Las credenciales ingresadas no son validas.");
        } else {
            // we need to return a toke and refresh token...
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(addUserDTO.getCustomerid());

            //login successfull...
            return JwtResponseDTO
                    .builder()
                    .accessToken(tokenDTO.getToken())
                    .refreshToken(refreshToken.getRefreshtoken())
                    .build();
        }
    }

    @PostMapping("/validate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "validación exitosa."),
            @ApiResponse(responseCode = "400", description = "petición enviada de manera erronea."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    //public ResponseEntity<?> validateToken(@RequestParam(required = true) String token, @RequestHeader("auth-user-id") String header ) {
    public ResponseEntity<?> validateToken(@RequestParam(required = true) String token) {
        System.out.println("Validando token.....");
        TokenDTO tokenDTO = authService.validateToken(token);

        //if return null...
        if (tokenDTO == null) {
            //return ResponseEntity.badRequest().build();
            System.out.println("Token invalido.....");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("El token ingresado ha expirado o bien no está registrado.");
        } else {
            //token validado...
            System.out.println("Token valido.....");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(tokenDTO);
        }
    }

    @PostMapping("/refreshToken")
    public JwtResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getAuthUser)
                .map(authUser -> {
                    String accessToken = jwtProvider.createToken(authUser);
                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshTokenRequestDTO.getRefreshToken())
                            .build();
                }).orElseThrow(() ->
                        new RuntimeException("El token refresh no existe.")
                );
    }

    @PostMapping("/newUser")
    public ResponseEntity<AuthUser> newUser(@RequestBody AddUserDTO addUserDTO) {
        AuthUser authUser = authService.newUser(addUserDTO);

        //if return null...
        if (authUser == null) {
            return ResponseEntity.badRequest().build();
        }

        //user created...
        return ResponseEntity.ok(authUser);
    }
}
