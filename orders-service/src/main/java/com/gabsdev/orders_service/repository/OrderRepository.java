package com.gabsdev.orders_service.repository;

import com.gabsdev.orders_service.model.Order;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final DynamoDbTemplate template;

    public List<Order> findByUserId(String userId) {
        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(k -> k.partitionValue(userId)))
                .build();

        // Assinatura: query(QueryEnhancedRequest, Class<T>)
        return template.query(request, Order.class)
                .items()
                .stream()
                .toList();
    }

    public void save(Order order) {
        template.save(order);
    }
}
