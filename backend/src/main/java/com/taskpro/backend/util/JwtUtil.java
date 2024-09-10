package com.taskpro.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    private final Key key;
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;

    public JwtUtil() {
        String secret = "a-secret-key-01234567890123456789";
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public static long getExpirationTime() {
        return EXPIRATION_TIME;
    }

    public String generateToken(Long userId) {
        return generateToken(new HashMap<>(), userId);
    }

    public String generateToken(Map<String, Object> extraClaims, Long userId) {
        return buildToken(extraClaims, userId);
    }

    private String buildToken(Map<String, Object> extraClaims, Long userId) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userId.toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Boolean validateToken(String token, String userId) {
        final String extractedUserId = extractUserId(token);
        return (extractedUserId.equals(userId) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
