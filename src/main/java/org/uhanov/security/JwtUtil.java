package org.uhanov.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.uhanov.exception.InvalidRoleException;
import org.uhanov.exception.InvalidTokenException;
import org.uhanov.model.user.Role;
import org.uhanov.model.user.User;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtil {
    public static final long DEFAULT_EXPIRATION_TIME = 100000 * 60 * 24;
    @Value("${jwt.key}")
    private String jwtSigningKey;

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Role extractRole(String token) {
        Claims allClaims = extractAllClaims(token);
        String roleName = allClaims.get("role", String.class);
        try {
            return Role.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new InvalidTokenException("В токене должна быть указана роль");
        }
    }

    public UUID extractId(String tokenBefore) {
        String token;
        if (tokenBefore.startsWith("Bearer ")) {
            token = tokenBefore.substring(7);
        } else {
            token = tokenBefore;
        }
        Claims allClaims = extractAllClaims(token);
        String uuidAsString = allClaims.get("id", String.class);
        try {
            return UUID.fromString(uuidAsString);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new InvalidTokenException("В токене должен быть указан id");
        }
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        userClaims(claims, userDetails);

        return generateToken(claims, userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + DEFAULT_EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    private Role extractRole(UserDetails userDetails) {
        try {
            var authorities = userDetails.getAuthorities()
                    .stream()
                    .findFirst()
                    .get();
            return Role.valueOf(authorities.getAuthority());
        } catch (IllegalArgumentException | NoSuchElementException e) {
            e.printStackTrace();
            throw new InvalidTokenException();
        }
    }

    private void userClaims(Map<String, Object> claims, UserDetails userDetails) {
        if (userDetails instanceof User) {
            User user = (User) userDetails;
            claims.put("id", user.getId().toString());
            claims.put("role", user.getRole().name());
            return;
        }
        throw new InvalidRoleException();
    }
}
