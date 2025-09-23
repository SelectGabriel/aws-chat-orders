package com.gabsdev.users_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import com.gabsdev.users_service.messaging.dto.OrderQueryReply;

@Configuration
public class SqsCorrelationConfig {

    @Bean
    @NonNull // o bean nunca é nulo
    public ConcurrentHashMap<String, CompletableFuture<OrderQueryReply>> correlationMap() {
        return new ConcurrentHashMap<>();
    }
}