package project;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

public class JwtUtil {

    private static final String SECRET_KEY =
            "mySecretKeyForJwtAuthenticationProject123456789";

    private static final Key key =
            Keys.hmacShaKeyFor(
                    SECRET_KEY.getBytes(StandardCharsets.UTF_8)
            );

    // Generate JWT token
    public static String generateToken(String email, String role) {

        String token = Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key)
                .compact();

        return token;
    }

    // Validate JWT token
    public static void validateToken(String token) {

        Jwts.parser()
        
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token);
    }
}