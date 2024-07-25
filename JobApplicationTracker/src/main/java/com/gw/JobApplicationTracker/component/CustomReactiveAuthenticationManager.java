package com.gw.JobApplicationTracker.component;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.gw.JobApplicationTracker.model.CustomAuthenticationToken;
import com.gw.JobApplicationTracker.model.UserPrincipal;
import com.gw.JobApplicationTracker.service.CustomUserDetailsService;

import reactor.core.publisher.Mono;

@Component
public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private static final Logger logger = LoggerFactory.getLogger(CustomReactiveAuthenticationManager.class);

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService _userService;

    private PasswordEncoder passwordEncoder;

    public CustomReactiveAuthenticationManager(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        logger.warn("Now in authenticate");

        String token = (String) authentication.getCredentials();
        if (jwtTokenProvider.validateToken(token)) {

            logger.warn("Token is valid in AuthenticationManager.authenticate");

            return Mono.just(jwtTokenProvider.getAuthentication(token));
                    // .doOnNext(auth -> {
                    //     // Refresh token if still valid
                    //     String newToken = jwtTokenProvider.generateToken(new UserPrincipal(jwtTokenProvider.getUserIdFromToken(token), 
                    //                         jwtTokenProvider.getUsernameFromToken(token), 
                    //                         null, 
                    //                         jwtTokenProvider.getAuthoritiesFromToken(token)));
                    //     ((ServerWebExchange) authentication.getDetails()).getResponse().getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + newToken);
                    // });
        }
        else{

            logger.warn("Token is invalid, go to db");

            String username = authentication.getName();
            String password = authentication.getCredentials().toString();

            return _userService.findByUsernameWithId(username)

                .flatMap(userDetails -> {
                    if (passwordEncoder.matches(password, userDetails.getPassword())) {
                        return Mono.just(new CustomAuthenticationToken(userDetails.getId(), username, null, userDetails.getAuthorities()));
                    } else {
                        return Mono.error(new BadCredentialsException("Invalid Credentials"));
                    }
                });
        }
    }
}