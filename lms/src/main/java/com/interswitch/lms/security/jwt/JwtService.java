package com.interswitch.lms.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * Generate JWT Token (kept for backward compatibility)
     */
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Generate Token + Expiration
     */
    public TokenWithExpiry generateTokenWithExpiry(String email, String role) {
        Date now = new Date(System.currentTimeMillis());
        Date expiresAt = new Date(now.getTime() + jwtExpiration);

        String token = Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiresAt)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();

        return new TokenWithExpiry(token, expiresAt.toInstant());
    }

    /**
     * Extract Email (Subject)
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract Role
     */
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    /**
     * Extract Expiration
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Generic Claim Extractor
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Validate Token
     */
    public boolean isTokenValid(String token, String email) {
        final String tokenEmail = extractEmail(token);
        return (tokenEmail.equals(email) && !isTokenExpired(token));
    }

    /**
     * Check if Token is Expired
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Parse All Claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Generate Signing Key
     */
    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Record to return token + expiration together
     */
    public record TokenWithExpiry(String token, java.time.Instant expiresAt) {
    }
}