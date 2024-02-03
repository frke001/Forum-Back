package org.unibl.etf.forumback.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.unibl.etf.forumback.models.dto.JwtUserDTO;
import org.unibl.etf.forumback.repositories.BlacklistRepository;
import org.unibl.etf.forumback.services.BlacklistService;
import org.unibl.etf.forumback.services.JwtService;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    private final BlacklistService blacklistService;
    private final BlacklistRepository blacklistRepository;
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    public JwtServiceImpl(BlacklistService blacklistService, BlacklistRepository blacklistRepository) {
        this.blacklistService = blacklistService;
        this.blacklistRepository = blacklistRepository;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(JwtUserDTO userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            JwtUserDTO userDetails
    ) {
        return buildToken(extraClaims, userDetails);
    }


    private String buildToken(
            Map<String, Object> extraClaims,
            JwtUserDTO userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setId(userDetails.getId().toString())
                .setSubject(userDetails.getUsername())
                .claim("role", userDetails.getRole().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token) && !blacklistService.existsByToken(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    @Scheduled(cron = "0 30 20 * * ?")
    public void clearOutdatedBlacklist() {
        var all = blacklistRepository.findAll();
        all.stream().forEach(el->{
            if(isTokenExpired(el.getToken())){
                blacklistRepository.delete(el);
            }
        });
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
