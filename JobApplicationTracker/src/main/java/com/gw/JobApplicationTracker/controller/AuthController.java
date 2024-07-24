package com.gw.JobApplicationTracker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gw.JobApplicationTracker.component.JwtTokenProvider;
import com.gw.JobApplicationTracker.model.LoginRequest;
import com.gw.JobApplicationTracker.model.LoginResponse;
import com.gw.JobApplicationTracker.model.UserPrincipal;
import com.gw.JobApplicationTracker.service.CloudFlareD1Service;
import com.gw.JobApplicationTracker.service.CustomUserDetailsService;

import reactor.core.publisher.Mono;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;



@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(CloudFlareD1Service.class);

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/")
    public String home(){

        return "index";
    }

    @GetMapping("/login")
    public String login(){

        return "login";
    }

    // @PostMapping("/login")
    // public Mono<ResponseEntity<?>> authenticateUser(@RequestBody LoginRequest loginRequest) {

    //     logger.warn("Login Request: {}", loginRequest.getUsername());

    //     return userDetailsService.findByUsername(loginRequest.getUsername())

    //             .flatMap(userDetails -> {
                    
    //                 if (passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {

    //                     String token = jwtTokenProvider.generateToken((UserPrincipal) userDetails);
    //                     logger.warn("Login Success: {}", token );
    //                     return Mono.just(ResponseEntity.ok(new LoginResponse(token)));
    //                 } 
    //                 else {
    //                     return Mono.just(ResponseEntity.badRequest().body("Invalid username or password"));
    //                 }
    //             });
    // }
}