package com.gw.JobApplicationTracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired
    private CloudFlareD1Service _d1Service;

    @Override
    public UserDetails loadUserByUsername(String username){

        var user = _d1Service.GetUserDetails(username);

        if (user != null){

            return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .build();
        }
        else{

            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}