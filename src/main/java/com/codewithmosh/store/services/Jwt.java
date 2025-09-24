package com.codewithmosh.store.services;

import com.codewithmosh.store.entities.Roles;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;

import javax.crypto.SecretKey;
import java.util.Date;

public class Jwt {
    private final Claims claims;
    private final SecretKey secretKey;
    public Jwt(Claims claims, SecretKey secretKey) {
        this.claims = claims;
        this.secretKey = secretKey;
    }
    public boolean isExpired() {
        return claims.getExpiration().before(new Date());
    }
    public Long getUserId() {
        return Long.valueOf(String.valueOf(claims.getSubject()));
    }
    public Roles getRole() {
        return Roles.valueOf(claims.get("role").toString());
    }
    public String toString() {
        return Jwts.builder().claims(claims).signWith(secretKey).compact();
    }
}
