package com.gw.JobApplicationTracker.component;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;

import com.gw.JobApplicationTracker.model.CustomAuthenticationToken;
import com.gw.JobApplicationTracker.model.UserPrincipal;

import reactor.core.publisher.Mono;

public class JwtAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationSuccessHandler.class);

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationSuccessHandler() {
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange exchange, Authentication authentication) {

        String token = jwtTokenProvider.generateToken(new UserPrincipal(((CustomAuthenticationToken) authentication).getUserId(), 
                                authentication.getPrincipal().toString(),
                                null, 
                                authentication.getAuthorities()));
        exchange.getExchange().getResponse().getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        return Mono.fromRunnable(() -> {
            exchange.getExchange().getResponse().setStatusCode(HttpStatus.FOUND);
            exchange.getExchange().getResponse().getHeaders().setLocation(URI.create("/dashboard"));
        });
    }
}