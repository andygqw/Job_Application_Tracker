package com.gw.JobApplicationTracker.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
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


    @Value("${cf.account.id}")
    private String account_id;

    @Value("${cf.database.id}")
    private String database_id;

    @Value("${cf.database.token}")
    private String database_token;

    private static String END_POINT;

    private final WebClient webClient;

    public CloudFlareD1Service(){

        END_POINT = "https://api.cloudflare.com/client/v4/accounts/"+ 
                        "account_id" + 
                        "/d1/database/" + 
                        "database_id" +
                        "/raw";

        webClient = WebClient.builder()
                .baseUrl(END_POINT)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Authorization", "Bearer " + "database_token")
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