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
import org.uhanov.exception.NoRoleException;
import org.uhanov.model.Creator;
import org.uhanov.model.Staff;
import org.uhanov.model.User;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

@Component
public class JwtUtil {
    public static final long DEFAULT_EXPIRATION_TIME = 100000 * 60 * 24;
//    @Value("${jwt.key}")
    private String jwtSigningKey = "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327855";

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Role extractRole(String token) {
        Claims allClaims = extractAllClaims(token);
        String roleName = allClaims.get("role", String.class);
        try {
            return Role.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new NoRoleException();
        }
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        Role role = extractRole(userDetails);
        switch (role) {
            case USER:
                userClaims(claims, userDetails);
                break;
            case STAFF:
                staffClaims(claims, userDetails);
                break;
            case CREATOR:
                creatorClaims(claims, userDetails);
                break;
        }
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
                .build()
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
            throw new NoRoleException();
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

    private void creatorClaims(Map<String, Object> claims, UserDetails userDetails) {
        if (userDetails instanceof Creator) {
            Creator creator = (Creator) userDetails;
            claims.put("id", creator.getId().toString());
            claims.put("role", creator.getRole().name());
            return;
        }
        throw new InvalidRoleException();
    }

    private void staffClaims(Map<String, Object> claims, UserDetails userDetails) {
        if (userDetails instanceof Staff) {
            Staff staff = (Staff) userDetails;
            claims.put("id", staff.getId().toString());
            claims.put("role", staff.getRole().name());
            return;
        }
        throw new InvalidRoleException();
    }
}
