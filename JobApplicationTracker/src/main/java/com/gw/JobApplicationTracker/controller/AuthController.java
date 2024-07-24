package com.gw.JobApplicationTracker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.gw.JobApplicationTracker.service.CloudFlareD1Service;


@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(CloudFlareD1Service.class);

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