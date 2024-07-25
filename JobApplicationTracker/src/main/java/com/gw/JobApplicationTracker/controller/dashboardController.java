package com.gw.JobApplicationTracker.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gw.JobApplicationTracker.model.CustomAuthenticationToken;

import reactor.core.publisher.Mono;

@Controller
public class DashboardController {


    @GetMapping("/dashboard")
    public Mono<String> dashboard(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("id", ((CustomAuthenticationToken) authentication).getUserId());
        return Mono.just("dashboard");
    }

    @GetMapping("/test")
    public Mono<String> test() {
        return Mono.just("test");
    }

}