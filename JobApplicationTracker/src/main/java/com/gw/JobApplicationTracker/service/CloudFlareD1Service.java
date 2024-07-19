package com.gw.JobApplicationTracker.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.gw.JobApplicationTracker.component.Utilities;
import com.gw.JobApplicationTracker.model.D1QueryRequestBody;
import com.gw.JobApplicationTracker.model.RawD1QueryResponse;
import com.gw.JobApplicationTracker.model.User;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.util.Iterator;

@Service
public class CloudFlareD1Service {

    private static String END_POINT;

    private static String BEARER_TOKEN;

    private final WebClient.Builder webClientBuilder;

    private D1QueryRequestBody requestBody;

    public CloudFlareD1Service(){

        END_POINT = "";
        BEARER_TOKEN = "";

        webClientBuilder = WebClient.builder();
        requestBody = new D1QueryRequestBody();
    }

    @Async
    public CompletableFuture<JsonNode> QueryD1(String query){

        WebClient webClient = webClientBuilder.build();

        return webClient.post()
                .uri(END_POINT)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + BEARER_TOKEN)
                .body(Mono.just(requestBody), D1QueryRequestBody.class)
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
                        ObjectMapper objectMapper = new ObjectMapper();
                        return objectMapper.readTree(response);
                    }
                    catch(Exception e){

                        throw new RuntimeException("Error parsing JSON response", e);
                    }
                })
                .toFuture();
    }

    @Async
    public User GetUserDetails(String username){

        String query = String.format("SELECT * FROM %s WHERE username = '%s'", Utilities.D1_DATABASE_NAME, username);

        QueryD1(query).thenApply(response -> {

            if(response.path(Utilities.D1_SUCCESS).asBoolean() == true){

                JsonNode user = response.path(Utilities.D1_RESULT)
                                                .get(0)
                                                .path(Utilities.D1_RESULTS)
                                                .get(0)
                                                .path(Utilities.D1_ROWS);

                if(user.has(0)){

                    user = user.get(0);
                    User newUser = new User(user.get(0).asLong(), user.get(2).asText(), user.get(5).asText());
                    return newUser;
                }
                else{

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