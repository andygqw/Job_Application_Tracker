package com.gw.JobApplicationTracker.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gw.JobApplicationTracker.component.Utilities;
import com.gw.JobApplicationTracker.model.UserPrincipal;

import reactor.core.publisher.Mono;



@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService{

	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private CloudFlareD1Service _d1Service;

    @Override
    public Mono<UserDetails> findByUsername(String username){

        String query = String.format("SELECT * FROM %s WHERE username = '%s'", Utilities.D1_TABLE_USERS, username);

        ObjectMapper objectMapper = new ObjectMapper();

        logger.warn("Already in findByUsername");

        return _d1Service.GetQueryMono(query)
                .map(ResponseEntity::getBody)
                .flatMap(body -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(body);
                        return Mono.just(jsonNode);
                    } catch (JsonProcessingException e) {
                        return Mono.error(new RuntimeException("Error processing JSON", e));
                    }
                })
                .flatMap(body -> {

                    try{
                        return Mono.just(body.findPath(Utilities.D1_ROWS).get(0));
                    }
                    catch(Exception ex){

                        return Mono.error(new UsernameNotFoundException("User not found"));
                    }
                })
                .map(user -> UserPrincipal.create(User.withUsername(user.get(5).asText())
                                                .password(user.get(2).asText())
                                                .roles(Utilities.ROLE_USER)
                                                .build(), user.get(1).asInt()))
                .cast(UserDetails.class)
                .doOnError(WebClientResponseException.class, ex -> {
                    logger.warn("Error response: " + ex.getStatusCode(), ex);
                })
                .doOnError(IOException.class, ex -> {
                    logger.warn(ex.getMessage());
                })
                .doOnError(Throwable.class, ex -> {
                    logger.error("Unexpected error: ", ex);
                });
    }

    public Mono<UserPrincipal> findByUsernameWithId(String username){

        String query = String.format("SELECT * FROM %s WHERE username = '%s'", Utilities.D1_TABLE_USERS, username);

        ObjectMapper objectMapper = new ObjectMapper();

        logger.warn("Already in findByUsernamewithId");

        return _d1Service.GetQueryMono(query)
                .map(ResponseEntity::getBody)
                .flatMap(body -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(body);
                        return Mono.just(jsonNode);
                    } catch (JsonProcessingException e) {
                        return Mono.error(new RuntimeException("Error processing JSON", e));
                    }
                })
                .flatMap(body -> {

                    try{
                        return Mono.just(body.findPath(Utilities.D1_ROWS).get(0));
                    }
                    catch(Exception ex){

                        return Mono.error(new UsernameNotFoundException("User not found"));
                    }
                })
                .map(user -> UserPrincipal.create(User.withUsername(user.get(5).asText())
                                                .password(user.get(2).asText())
                                                .roles(Utilities.ROLE_USER)
                                                .build(), user.get(0).asInt()))
                .doOnError(WebClientResponseException.class, ex -> {
                    logger.warn("Error response: " + ex.getStatusCode(), ex);
                })
                .doOnError(IOException.class, ex -> {
                    logger.warn(ex.getMessage());
                })
                .doOnError(Throwable.class, ex -> {
                    logger.error("Unexpected error: ", ex);
                });
    }

    public Mono<UserDetails> findById(int id){

        String query = String.format("SELECT * FROM %s WHERE username = '%d'", Utilities.D1_TABLE_USERS, id);

        ObjectMapper objectMapper = new ObjectMapper();

        return _d1Service.GetQueryMono(query)
                .map(ResponseEntity::getBody)
                .flatMap(body -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(body);
                        return Mono.just(jsonNode);
                    } catch (JsonProcessingException e) {
                        return Mono.error(new RuntimeException("Error processing JSON", e));
                    }
                })
                .flatMap(body -> {

                    try{
                        return Mono.just(body.findPath(Utilities.D1_ROWS).get(0));
                    }
                    catch(Exception ex){

                        return Mono.error(new UsernameNotFoundException("User not found"));
                    }
                })
                .map(user -> User.withUsername(user.get(5).asText())
                            .password(user.get(2).asText())
                            .roles(Utilities.ROLE_USER)
                            .build()
                )
                .doOnError(WebClientResponseException.class, ex -> {
                    logger.warn("Error response: " + ex.getStatusCode(), ex);
                })
                .doOnError(IOException.class, ex -> {
                    logger.warn(ex.getMessage());
                })
                .doOnError(Throwable.class, ex -> {
                    logger.error("Unexpected error: ", ex);
                });
    }
}