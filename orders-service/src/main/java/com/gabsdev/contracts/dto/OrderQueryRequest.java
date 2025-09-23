package com.gabsdev.contracts.dto;

public record OrderQueryRequest(String userId, String correlationId, String replyToQueue) {}