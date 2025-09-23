package com.gabsdev.contracts.dto;

import java.util.List;
public record OrderQueryReply(String correlationId, List<OrderSummary> orders) {
    public record OrderSummary(String orderId, String status) {}
}