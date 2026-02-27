package org.mongodb.springboot.kitchensinkmordernization.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.mongodb.springboot.kitchensinkmordernization.entites.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class AuthUtil {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    private SecretKey getJwtSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
    }

    public String generateJwtToken(Member member) {
        return Jwts.builder()
                .subject(member.getName())
                .claim("userId ", member.getId()
                        .toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(getJwtSecretKey())
                .compact();

    }

    public String getUserNameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getJwtSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
