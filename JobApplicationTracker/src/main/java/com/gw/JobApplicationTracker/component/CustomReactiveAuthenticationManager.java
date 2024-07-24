package com.gw.JobApplicationTracker.component;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.gw.JobApplicationTracker.model.UserPrincipal;
import com.gw.JobApplicationTracker.service.CloudFlareD1Service;
import com.gw.JobApplicationTracker.service.CustomUserDetailsService;

import reactor.core.publisher.Mono;

@Component
public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private static final Logger logger = LoggerFactory.getLogger(CloudFlareD1Service.class);

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService _userService;

    public CustomReactiveAuthenticationManager(JwtTokenProvider jwtTokenProvider) {
        logger.warn("CustomReactiveAuthenticationManager constructor");
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        logger.warn("Start of authenticate : " + authentication.toString());
        String token = (String) authentication.getCredentials();
        logger.warn("Token in AuthenticationManager.authenticate: " + token);
        if (jwtTokenProvider.validateToken(token)) {

            logger.warn("Token is valid in AuthenticationManager.authenticate");

            return Mono.just(jwtTokenProvider.getAuthentication(token))
                    .doOnNext(auth -> {
                        // Refresh token if still valid
                        String newToken = jwtTokenProvider.generateToken((UserPrincipal) auth.getPrincipal());
                        ((ServerWebExchange) authentication.getDetails()).getResponse().getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + newToken);
                    });
        }
        logger.warn("End of authenticate");
        return Mono.just(authentication);
    }
}