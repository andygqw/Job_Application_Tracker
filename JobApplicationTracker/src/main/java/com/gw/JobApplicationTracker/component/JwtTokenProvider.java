package com.gw.JobApplicationTracker.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.gw.JobApplicationTracker.model.CustomAuthenticationToken;
import com.gw.JobApplicationTracker.model.UserPrincipal;
import com.gw.JobApplicationTracker.service.CloudFlareD1Service;

import java.util.Collection;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(CloudFlareD1Service.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(UserPrincipal userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim(Utilities.COOKIE_USER_ID, userDetails.getId())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        
        Claims claims = getTokenBody(token);
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(claims.get(Utilities.COOKIE_ROLES).toString());
        User principal = new User(claims.getSubject(), "", authorities);
        return new CustomAuthenticationToken(getUserIdFromToken(token), principal, token, authorities);
    }

    // Reduce use of this method
    public String getUsernameFromToken(String token) {

        Claims claims = getTokenBody(token);

        return claims.getSubject();
    }

    public int getUserIdFromToken(String token) {

        Claims claims = getTokenBody(token);

        logger.warn(claims.toString());

        return claims.get(Utilities.COOKIE_USER_ID, Integer.class);
    }

    public boolean validateToken(String token) {
        try {

            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return !isTokenExpired(token);
        } 
        catch (Exception ex) {
            
            logger.warn("Token validation error", ex.getMessage());;
        }
        return false;
    }

    public boolean isTokenExpired(String token) {
        Date expiration = getTokenBody(token)
                .getExpiration();
        return expiration.before(new Date());
    }

    private Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }   
}