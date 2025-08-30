package com.timerdar.farmCrm.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET = "your-very-secret-key-your-very-secret-key"; // минимум 256 бит для HS256

    private static final long EXPIRATION_MS = 3600000; // 1 час

    // Получение ключа (удобнее вынести в переменную)
    private static final SecretKey secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());

    // Генерация JWT-токена
    public static String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(secretKey)
                .compact();
    }

    // Извлечение username из токена
    public static String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    // Проверка валидности токена и срока жизни
    public static boolean validateToken(String token, String username) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return (claims.getSubject().equals(username) && !isTokenExpired(claims));
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isTokenExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }
}
