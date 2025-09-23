package com.gabsdev.users_service.config;

import com.gabsdev.contracts.dto.OrderQueryReply;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class SqsCorrelationConfig {

    @Bean
    public Map<String, CompletableFuture<OrderQueryReply>> correlationMap() {
        return new ConcurrentHashMap<>();
    }
}