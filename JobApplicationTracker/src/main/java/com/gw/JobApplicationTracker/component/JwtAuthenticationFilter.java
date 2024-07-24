package com.gw.JobApplicationTracker.component;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.gw.JobApplicationTracker.service.CloudFlareD1Service;

import reactor.core.publisher.Mono;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Component
public class JwtAuthenticationFilter extends AuthenticationWebFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(CustomReactiveAuthenticationManager authenticationManager) {
        super(authenticationManager);
        setServerAuthenticationConverter(new JwtAuthenticationConverter());
    }

    private class JwtAuthenticationConverter implements ServerAuthenticationConverter {
        @Override
        public Mono<Authentication> convert(ServerWebExchange exchange) {
            logger.warn("We are in convert");
            String token = getJwtFromRequest(exchange.getRequest());
            if (token != null) {

                logger.warn("Successfully extract token from the request in AuthenticationWebFilter.Convert");
                return Mono.just(new UsernamePasswordAuthenticationToken(null, token))
                            .cast(Authentication.class);
            }
            return Mono.empty();
        }

        private String getJwtFromRequest(ServerHttpRequest request) {
            String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7);
            }
            return null;
        }
    }
}
