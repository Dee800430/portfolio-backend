package com.back.conifig;

import com.back.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    // =========================
    // GENERATE TOKEN
    // =========================
    public String generateToken(User user) {

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getUserId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hours
                .signWith(getSigningKey())
                .compact();
    }

    // =========================
    // EXTRACT USERNAME
    // =========================
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // =========================
    // EXTRACT USER ID
    // =========================
    public Long extractUserId(String token) {
        return getClaims(token).get("userId", Long.class);
    }

    // =========================
    // VALIDATE TOKEN
    // =========================
    public boolean isTokenValid(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // =========================
    // GET CLAIMS
    // =========================
    private Claims getClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // =========================
    // SIGNING KEY
    // =========================
    private SecretKey getSigningKey() {

        // IMPORTANT: supports strong secret keys safely
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}