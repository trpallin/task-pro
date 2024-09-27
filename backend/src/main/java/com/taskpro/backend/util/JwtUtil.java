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
    private final Key accessKey;
    private final Key refreshKey;
    private static final long ACCESS_EXPIRATION_TIME = 1000 * 60 * 15;
    private static final long REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;

    public JwtUtil() {
        String accessSecret = "a-secret-key-for-access-token-0123456789";
        String refreshSecret = "a-secret-key-for-refresh-token-9876543210";
        byte[] accessKeyBytes = accessSecret.getBytes(StandardCharsets.UTF_8);
        byte[] refreshKeyBytes = refreshSecret.getBytes(StandardCharsets.UTF_8);
        this.accessKey = Keys.hmacShaKeyFor(accessKeyBytes);
        this.refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
    }

    public static long getExpirationTime() {
        return ACCESS_EXPIRATION_TIME;
    }

    public String generateAccessToken(Long userId) {
        return buildToken(new HashMap<>(), userId, ACCESS_EXPIRATION_TIME, accessKey);
    }

    public String generateRefreshToken(Long userId) {
        return buildToken(new HashMap<>(), userId, REFRESH_EXPIRATION_TIME, refreshKey);
    }

    private String buildToken(Map<String, Object> extraClaims, Long userId, long expirationTime, Key key) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userId.toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    public String extractUserIdFromAccessToken(String token) {
        return extractUserId(token, accessKey);
    }

    public String extractUserIdFromRefreshToken(String token) {
        return extractUserId(token, refreshKey);
    }

    private String extractUserId(String token, Key key) {
        return extractClaim(token, Claims::getSubject, key);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, Key key) {
        final Claims claims = extractAllClaims(token, key);
        return claimsResolver.apply(claims);
    }

    private Boolean validateToken(String token, String userId, Key key) {
        final String extractedUserId = extractUserId(token, key);
        return (extractedUserId.equals(userId) && !isTokenExpired(token, key));
    }

    private Boolean isTokenExpired(String token, Key key) {
        return extractExpiration(token, key).before(new Date());
    }

    private Date extractExpiration(String token, Key key) {
        return extractClaim(token, Claims::getExpiration, key);
    }

    private Claims extractAllClaims(String token, Key key) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
