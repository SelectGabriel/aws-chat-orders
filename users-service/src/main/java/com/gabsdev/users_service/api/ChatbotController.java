package com.gabsdev.users_service.api;

import com.gabsdev.users_service.messaging.SqsGateway;
import com.gabsdev.users_service.messaging.dto.OrderQueryReply;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final SqsGateway gateway;

    @GetMapping("/orders")
    public ResponseEntity<OrderQueryReply> getOrders(@RequestParam String userId) {
        return ResponseEntity.ok(gateway.requestOrdersForUser(userId));
    }
}

