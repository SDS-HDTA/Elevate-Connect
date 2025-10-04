package org.sds.elevateconnect.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtService {
    @Value("${jwt.sign-key}")
    private String signKey;
    
    private static final Long expire = 1000L * 60 * 60 * 24 * 30; // 30 days

    public String generateJwt(UserDetails userDetails, JwtClaims extraClaims) {
        try {
            return Jwts.builder()
                    .setClaims(getClaimsMap(extraClaims))
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expire))
                    .signWith(SignatureAlgorithm.HS256, signKey)
                    .compact();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = getEmail(token);
        boolean isEmailCorrect = email.equals(userDetails.getUsername());

        return isEmailCorrect && isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractTokenExpiration(token).before(new Date());
    }

    public String getEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractTokenExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(signKey)
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Map<String, Object> getClaimsMap(JwtClaims claims) throws IllegalAccessException {
        Map<String, Object> claimsMap = new HashMap<>();
        // Fetch all fields on the claims object
        Field[] fields = claims.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            claimsMap.put(field.getName(), field.get(claims));
        }

        return claimsMap;
    }
}
