package com.gw.JobApplicationTracker.service;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.gw.JobApplicationTracker.component.Utilities;
import com.gw.JobApplicationTracker.model.D1QueryRequestBody;
import com.gw.JobApplicationTracker.model.User;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class CloudFlareD1Service {

    private static final Logger logger = LoggerFactory.getLogger(CloudFlareD1Service.class);

    private static String END_POINT;

    private static String BEARER_TOKEN;

    private final WebClient webClient;

    private D1QueryRequestBody requestBody;

    public CloudFlareD1Service(){

        END_POINT = "";
        BEARER_TOKEN = "";

        webClient = WebClient.builder()
                .baseUrl(END_POINT)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Authorization", "Bearer " + BEARER_TOKEN)
                .build();
    }

    public CompletableFuture<JsonNode> QueryD1(String query) throws Exception{

        String body = "{\n  \"params\": [\n ],\n  \"sql\": \"" + query + "\"\n}";

        return webClient.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                // .doOnError(WebClientResponseException.class, ex -> {
                //     logger.log(Level.SEVERE, "Error response status: " + ex.getStatusCode(), ex);
                // })
                // .doOnError(Throwable.class, ex -> {
                //     logger.log(Level.SEVERE, "Unexpected error: ", ex);
                // })
                // .onErrorResume(ex -> {
                //     RawD1QueryResponse errorResponse = new RawD1QueryResponse();
                //     errorResponse.setSuccess(false);
                //     errorResponse.setErrors(List.of("An error occurred"));
                //     return Mono.just(errorResponse);
                // })
                .map(response -> {

                    try{
                        logger.warn(response);
                        ObjectMapper objectMapper = new ObjectMapper();
                        return objectMapper.readTree(response);
                    }
                    catch(Exception e){

                        throw new RuntimeException("Error parsing JSON response", e);
                    }
                })
                .toFuture();
    }

    public User GetUserDetails(String username) throws Exception{

        logger.warn("We now in D1Service.GetUserDetails");

        String query = String.format("SELECT * FROM %s WHERE username = '%s'", Utilities.D1_TABLE_USERS, username);

        QueryD1(query).thenApply(response -> {

            if(response.path(Utilities.D1_SUCCESS).asBoolean() == true){

                JsonNode user = response.path(Utilities.D1_RESULT)
                                                .get(0)
                                                .path(Utilities.D1_RESULTS)
                                                //.get(0)
                                                .path(Utilities.D1_ROWS);

                logger.warn("Done parsing");

                if(user.has(0)){

                    user = user.get(0);
                    logger.info(user.toString());
                    User newUser = new User(user.get(0).asLong(), user.get(2).asText(), user.get(5).asText());
                    return newUser;
                }
                else{

                    logger.warn("User not found");
                    return null;
                }
            }
            else{

                throw new RuntimeException("Error querying D1 database" + response.path(Utilities.D1_ERRORS)
                                            .get(0)
                                            .path(Utilities.D1_MESSAGE)
                                            .asText());
            }
        });

        return null;
    }
}