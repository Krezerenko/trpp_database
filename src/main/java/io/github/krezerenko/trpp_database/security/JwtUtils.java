package io.github.krezerenko.trpp_database.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils
{
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpirationMs;
    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpirationMs;

    private SecretKey signingKey;

    @PostConstruct
    public void init()
    {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateAccessToken(String username)
    {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpirationMs))
                .signWith(signingKey, Jwts.SIG.HS512)
                .compact();
    }

    public String generateRefreshToken(String username)
    {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpirationMs))
                .signWith(signingKey, Jwts.SIG.HS512)
                .compact();
    }

    public String validateToken(String token)
    {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
