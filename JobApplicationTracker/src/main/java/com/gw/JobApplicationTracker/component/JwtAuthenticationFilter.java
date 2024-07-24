package com.gw.JobApplicationTracker.component;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.gw.JobApplicationTracker.service.CustomUserDetailsService;

import reactor.core.publisher.Mono;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Component
public class JwtAuthenticationFilter extends AuthenticationWebFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService customUserDetailsService) {
        super(authenticationManager -> Mono.empty());
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
        setServerAuthenticationConverter(new JwtAuthenticationConverter());
    }

    private class JwtAuthenticationConverter implements ServerAuthenticationConverter {
        @Override
        public Mono<Authentication> convert(ServerWebExchange exchange) {
            String token = getJwtFromRequest(exchange.getRequest());
            if (token != null && jwtTokenProvider.validateToken(token)) {

                return Mono.just(jwtTokenProvider.getAuthentication(token));

                // return customUserDetailsService.findById(userId)
                //         .map(userDetails -> {
                //             UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                //             return authentication;
                //         })
                //         .doOnNext(authentication -> {
                //             // Refresh token if still valid
                //             String newToken = jwtTokenProvider.generateToken((UserDetails) authentication.getPrincipal());
                //             exchange.getResponse().getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + newToken);
                //         });
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
