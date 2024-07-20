package com.gw.JobApplicationTracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {


    @GetMapping("/dashboard")
    public String getAllApplications(){

        return "dashboard";
    }
}
