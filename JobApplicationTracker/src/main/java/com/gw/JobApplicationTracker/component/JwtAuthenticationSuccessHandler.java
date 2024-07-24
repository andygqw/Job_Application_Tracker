package com.gw.JobApplicationTracker.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.server.ServerWebExchange;

import com.gw.JobApplicationTracker.service.CustomUserDetailsService;

import java.net.URI;
import java.nio.file.attribute.UserPrincipal;

import reactor.core.publisher.Mono;

public class JwtAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationSuccessHandler.class);

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationSuccessHandler() {
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange exchange, Authentication authentication) {

        logger.warn(authentication.toString());

        String token = jwtTokenProvider.generateToken((com.gw.JobApplicationTracker.model.UserPrincipal) authentication.getPrincipal());
        exchange.getExchange().getResponse().getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        return Mono.fromRunnable(() -> {
            exchange.getExchange().getResponse().setStatusCode(HttpStatus.FOUND);
            exchange.getExchange().getResponse().getHeaders().setLocation(URI.create("/dashboard"));
        });
    }
}