package com.em.taskmanager.services;

import com.em.taskmanager.dtos.CustomUserDetails;
import com.em.taskmanager.exceptions.ErrorResponseException;
import com.em.taskmanager.exceptions.ErrorStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.lang.Function;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.DateTimeException;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    private static final long TOKEN_EXPIRATION_PERIOD_MILLISECONDS = 100000L * 60 * 1000;
    private final SecretKey signingKey;

    public JwtService(@Value("${SECURITY_KEY}") String securityKey) {
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(securityKey));
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        if (userDetails instanceof CustomUserDetails customUserDetails) {
            claims.put("id", customUserDetails.getId());
        }

        try {
            // Преобразование OffsetDateTime в Date
            //OffsetDateTime конвертирую в Date, потому что библиотека,
            // используемая для создания JWT токена (например, JJWT),
            // требует, чтобы даты были в формате java.util.Date.
            claims.forEach((key, value) -> {
                if (value instanceof OffsetDateTime) {
                    OffsetDateTime dateTime = (OffsetDateTime) value;
                    claims.put(key, Date.from(dateTime.toInstant()));
                }
            });
        } catch (DateTimeException e) {
            throw new ErrorResponseException(ErrorStatus.TOKEN_DATETIME_ERROR);
        }


        try {
            return Jwts.builder()
                    .claims(claims)
                    .subject(userDetails.getUsername())
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_PERIOD_MILLISECONDS))
                    .signWith(signingKey, Jwts.SIG.HS256)
                    .compact();
        } catch (IllegalArgumentException e) {
            throw new ErrorResponseException(ErrorStatus.TOKEN_BUILD_ERROR);
        }
    }

    public boolean isTokenValid(String token, CustomUserDetails customUserDetails) {
        final Long userId = extractUserId(token);

        return (userId.equals(customUserDetails.getId())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("id", Long.class));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claimsResolver.apply(claims);
        } catch (JwtException | IllegalArgumentException e) {
            throw new ErrorResponseException(ErrorStatus.TOKEN_INVALID);
        }

    }

}
