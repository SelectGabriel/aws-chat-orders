package com.gabsdev.orders_service.api;

import com.gabsdev.orders_service.api.dto.CreateOrderRequest;
import com.gabsdev.orders_service.api.dto.CreateOrderResponse;
import com.gabsdev.orders_service.model.Order;
import com.gabsdev.orders_service.repository.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository repository;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> create(@Valid @RequestBody CreateOrderRequest req) {
        Order order = new Order();
        order.setUserId(req.userId());
        order.setOrderId(UUID.randomUUID().toString());
        order.setCreatedAt(Instant.now());
        order.setItems(req.items());
        order.setStatus(req.status());

        repository.save(order);
        log.info("Order criado: userId={} orderId={}", order.getUserId(), order.getOrderId());

        return ResponseEntity.ok(new CreateOrderResponse(order.getUserId(), order.getOrderId()));
    }

    @GetMapping
    public ResponseEntity<List<Order>> byUser(@RequestParam String userId) {
        return ResponseEntity.ok(repository.findByUserId(userId));
    }
}

