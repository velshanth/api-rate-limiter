package org.guvi.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private final long expirationMs;
    private final SecretKey secretKey;

    public JwtUtil(@Value("${app.jwt.expirationMinutes}") long expirationMs,
                   @Value("${app.jwt.secret}") String secret) {
        this.expirationMs = expirationMs * 1000L;
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        this.secretKey = Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(String userId){
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(userId)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    public boolean isTokenValid(String token){
        try{
            parseClaims(token);
            return true;
        } catch (JwtException ex){
            return false;
        }
    }

    public String extractUserId(String token) {
        return parseClaims(token).getSubject();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
