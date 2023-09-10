package com.unagra.auth.jwt.authservice.security;

import com.unagra.auth.jwt.authservice.entities.AuthUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtProvider {
    @Value("${ebanking.jwt.secretPassword}")
    private String secretPassword;

    @PostConstruct
    protected void init() {
        secretPassword = Base64.getEncoder().encodeToString(secretPassword.getBytes());
    }

    public String createToken(AuthUser authUser) {
        Map<String, Object> claims = new HashMap<>();
        claims = Jwts.claims().setSubject(authUser.getCustomerid().toString());
        claims.put("id", authUser.getId());
        Date now = new Date();
        //Date expirate = new Date(now.getTime() + 3600000);
        Date expirate = new Date(System.currentTimeMillis() + (1000 * 60 * 15)); //last part indicates minutes...
        return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(expirate).signWith(SignatureAlgorithm.HS256, secretPassword).compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretPassword).parseClaimsJws(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public String getUserNameFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secretPassword).parseClaimsJws(token).getBody().getSubject();
        } catch (Exception exception) {
            return "Token incorrecto";
        }
    }
}
