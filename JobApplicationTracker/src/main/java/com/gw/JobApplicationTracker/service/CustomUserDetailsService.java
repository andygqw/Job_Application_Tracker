package com.gw.JobApplicationTracker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gw.JobApplicationTracker.JobApplicationTrackerApplication;

import reactor.core.publisher.Mono;



@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService{

	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private CloudFlareD1Service _d1Service;

    @Override
    public Mono<UserDetails> findByUsername(String username){

        logger.warn("We are now in findByUsername");

        com.gw.JobApplicationTracker.model.User user = null;

        try {

            logger.warn(_d1Service.toString());
            user = _d1Service.GetUserDetails(username);

        } catch (Exception e) {
            
            logger.error(username, e.getMessage());
        }

        if (user != null){

            logger.warn("User: " + user.toString());

            return Mono.just(

                User.withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles("USER")
                    .build()
            );
        }
        else{

            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}