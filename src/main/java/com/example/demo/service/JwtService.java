package com.example.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateToken(UserDetails userDetails){
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);
        SecretKey key = getSigningKey();

        String token = Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();

        System.out.println(userDetails.getUsername());
        System.out.println(secret.substring(0, Math.min(20, secret.length())));
        return token;
    }
    public String extractUsername(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (Exception e) {
            System.out.println(" Ошибка extractUsername: " + e.getMessage());
            throw e;
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String username = claims.getSubject();
            Date expiration = claims.getExpiration();
            boolean isValid = username != null
                    && username.equals(userDetails.getUsername())
                    && expiration != null
                    && !expiration.before(new Date());

            System.out.println(" Проверка токена:");
            System.out.println("   username из токена: " + username);
            System.out.println("   username из UserDetails: " + userDetails.getUsername());
            System.out.println("   expiration: " + expiration);
            System.out.println("   current time: " + new Date());
            System.out.println("   isValid: " + isValid);

            return isValid;
        } catch (Exception e) {
            System.out.println("Ошибка isTokenValid: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
