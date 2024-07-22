package com.gw.JobApplicationTracker.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import reactor.core.publisher.Mono;

@Controller
public class DashboardController {


    @GetMapping("/dashboard")
    public Mono<String> dashboard(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        return Mono.just("dashboard");
    }
}