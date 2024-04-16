/*package Marketplace.config.security.token.impl;

import Marketplace.config.security.token.AccessToken;
import Marketplace.config.security.token.AccessTokenDecoder;
import Marketplace.config.security.token.AccessTokenEncoder;
import Marketplace.config.security.token.exception.InvalidAccessTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AccessTokenEncoderDecoderImpl implements AccessTokenEncoder, AccessTokenDecoder {

    private final Key key;

    public AccessTokenEncoderDecoderImpl(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    @Override
    public String encode(AccessToken accessToken){
        Map<String, Object> claims = new HashMap<>();
        if(!accessToken.getRole().isEmpty()){
            claims.put("role", accessToken.getRole());
        }
        if(accessToken.getUserId() != null){
            claims.put("userId", accessToken.getUserId());
        }

        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(accessToken.getSubject())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(360, ChronoUnit.MINUTES)))
                .addClaims(claims)
                .signWith(key)
                .compact();
    }

    @Override
    public AccessToken decode(String accessTokenEncoded) {
        try {
            Jwt<?, Claims> jwt = Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(accessTokenEncoded);
            Claims claims = jwt.getBody();

            String role = claims.get("role", String.class);
            Long userId = claims.get("userId", Long.class);

            return new AccessTokenImpl(claims.getSubject(), userId, role);
        } catch (JwtException e) {
            throw new InvalidAccessTokenException(e.getMessage());
        }
    }
}*/
