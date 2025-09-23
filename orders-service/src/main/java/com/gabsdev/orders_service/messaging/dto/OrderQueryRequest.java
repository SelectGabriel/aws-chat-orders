package com.gabsdev.orders_service.messaging.dto;

public record OrderQueryRequest(String userId, String correlationId, String replyToQueue) {}