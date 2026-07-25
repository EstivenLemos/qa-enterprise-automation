package com.smartstore.backend.security.jwt;

import com.smartstore.backend.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey signingKey;

    private final long accessTokenExpirationMs;

    private final long refreshTokenExpirationMs;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long accessTokenExpirationMs,
            @Value("${jwt.refresh-expiration}") long refreshTokenExpirationMs
    ) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    public String generateAccessToken(User user) {
        return buildToken(user, accessTokenExpirationMs);
    }

    public String generateRefreshToken(User user) {
        return buildToken(user, refreshTokenExpirationMs);
    }

    private String buildToken(User user, long expirationMs) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey)
                .compact();

    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isValid(String token) {

        try {
            Claims claims = parseClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }

    }

    private Claims parseClaims(String token) {

        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

}
