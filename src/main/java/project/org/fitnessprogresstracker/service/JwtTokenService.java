package project.org.fitnessprogresstracker.service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import project.org.fitnessprogresstracker.entities.RefreshToken;
import project.org.fitnessprogresstracker.entities.User;
import project.org.fitnessprogresstracker.repository.RefreshTokenRepository;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service

public class JwtTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserService userService;
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.token.lifetime}")
    private Duration jwtAccessLifetime;

    @Value("${jwt.refresh.token.lifetime}")
    private Duration jwtRefreshLifetime;


    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roleList = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roleList);

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtAccessLifetime.toMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtRefreshLifetime.toMillis());
        String username = userDetails.getUsername();
        String refreshJwt = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        User user = userService.findByUsername(username).get();

        RefreshToken refreshToken = RefreshToken.builder()
                .expiryDate(expiredDate)
                .user(user)
                .token(refreshJwt)
                .build();

        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUserUsername(username);
        optionalRefreshToken.ifPresent(token -> refreshToken.setId(token.getId()));
        refreshTokenRepository.save(refreshToken);
        return refreshJwt;
    }

    public boolean verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().before(new Date())) {
            refreshTokenRepository.delete(token);
            return false;
        }
        return true;
    }
}
