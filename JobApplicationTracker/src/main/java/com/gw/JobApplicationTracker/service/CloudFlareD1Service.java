package com.gw.JobApplicationTracker.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public CloudFlareD1Service(){

        END_POINT = "";
        BEARER_TOKEN = "";

        webClient = WebClient.builder()
                .baseUrl(END_POINT)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Authorization", "Bearer " + BEARER_TOKEN)
                .build();
    }

    public Mono<ResponseEntity<String>>  GetQueryMono (String query){

        String body = "{\n  \"params\": [\n ],\n  \"sql\": \"" + query + "\"\n}";

        return webClient.post()
                .bodyValue(body)
                .retrieve()                
                .toEntity(String.class);                
    }
}