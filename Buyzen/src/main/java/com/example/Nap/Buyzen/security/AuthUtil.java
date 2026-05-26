package com.example.Nap.Buyzen.security;

import com.example.Nap.Buyzen.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class AuthUtil {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;



    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user){
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role",user.getRole().name())
                .issuedAt(new Date())
                .signWith(getSecretKey())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .compact();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public String getUsernameFromToken(String token) {
        return extractClaims(token).getSubject();
    }

    public int getUserIdFromToken(String token) {
        return extractClaims(token).get("userId", Integer.class);
    }

}
