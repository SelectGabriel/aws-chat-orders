package com.gabsdev.orders_service.config;

import com.gabsdev.orders_service.model.Order;
import io.awspring.cloud.dynamodb.DynamoDbTableNameResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

@Configuration
public class DynamoConfig {

    @Bean
    DynamoDbTableNameResolver tableNameResolver(
            @Value("${dynamodb.table.orders}") String ordersTable) {

        return new DynamoDbTableNameResolver() {
            @Override
            public <T> @NonNull String resolve(@NonNull Class<T> type) {
                if (Order.class.equals(type)) return ordersTable;
                return type.getSimpleName();
            }
        };
    }
}