package com.sd.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class TokenUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;
    //generates new jwt token
    public String generateJWT(Authentication authentication) {
        String subject = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + Constants.JWT_EXPIRY_TIME);
        return Jwts.builder().setSubject(subject).setIssuedAt(currentDate).setExpiration(expirationDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();

    }
    //gives username
    public String extractUserName(String jwtToken){
        return extractClaim(jwtToken, Claims::getSubject);
    }
    //to set siging key
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    //extracts all claims from token
    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }
    //generic method to extract a specific claim/field
    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver){
        Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }
    //checks if token is expired
    public boolean isTokenValid(String jwtToken){
        Date expiryTime = extractClaim(jwtToken, Claims::getExpiration);
        return new Date().before(expiryTime);
    }

}
