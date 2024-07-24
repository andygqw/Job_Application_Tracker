package com.gw.JobApplicationTracker.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import com.gw.JobApplicationTracker.service.CloudFlareD1Service;

import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    private static final Logger logger = LoggerFactory.getLogger(CloudFlareD1Service.class);

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomReactiveAuthenticationManager authenticationManager;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = getJwtFromRequest(exchange);
        if (token != null && tokenProvider.validateToken(token)) {
            return authenticationManager.authenticate(new JwtAuthenticationToken(token))
                    .flatMap(authentication -> {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        return chain.filter(exchange);
                    });
        }
        return chain.filter(exchange);
    }

    private String getJwtFromRequest(ServerWebExchange exchange) {
        String bearerToken = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        logger.warn("JWT token not found in request");
        return null;
    }
}