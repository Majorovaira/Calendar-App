package by.innowise.calendarapp.security.utils;


import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${secret.key}")
    private String secret;

    @Value("${auth.token.life}")
    private long expirationAuthToken;

    @Value("${refresh.token.life}")
    private long expirationRefreshToken;



    public String generateToken(String userName) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("isAdmin", true);
        log.info("generate token with exp time " + expirationAuthToken);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationAuthToken))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();


    }

    public String getRefreshToken(String subject) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("isAdmin", true);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationRefreshToken))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public boolean validateToken(String authToken) {

        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        }
        catch (ExpiredJwtException e) {
            throw e;
        }

    }

    public String getUsernameFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return returnExpDate(token).before(new Date());
    }

    private Date returnExpDate(String token) {
       Date date = extractClaim(token, Claims::getExpiration);
        return date;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

}
