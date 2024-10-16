package com.marcos.desenvolvimento.desafio_tecnico.security.jwt;

import com.marcos.desenvolvimento.desafio_tecnico.security.service.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;



@Component
public class TokenUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenUtils.class);

    @Value("${api.app.jwtSecret}")
    private String jwtSecret;

    @Value("${api.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String getTokenFromRequest(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }

    public String getUserName(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey(jwtSecret))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private SecretKey getSigningKey(String string) {
        return Keys.hmacShaKeyFor(string.getBytes(StandardCharsets.UTF_8));
    }

    public String generateTokenFromUserDetails(UserDetailsImpl user) {
        return Jwts.builder()
                .claim("id", user.getId())
                .subject(user.getUsername())
                .claim("roles", user.getAuthorities())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(getSigningKey(jwtSecret))
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey(jwtSecret)).build().parseSignedClaims(token);
            return true;
        } catch (WeakKeyException | SecurityException e) {
            LOGGER.error("Assinatura do token inválida: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Token JWT inválido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("Token JWT expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Token JWT não suportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("As claims do JWT estão vazias!");
        }
        return false;
    }
}
