package com.gabsdev.orders_service.messaging;

import com.gabsdev.contracts.dto.OrderQueryReply;
import com.gabsdev.contracts.dto.OrderQueryRequest;
import com.gabsdev.orders_service.model.Order;
import com.gabsdev.orders_service.repository.OrderRepository;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrdersListener {

    private final OrderRepository repository;
    private final SqsTemplate sqsTemplate;

    @Value("${aws.sqs.reply-queue}")
    private String defaultReplyQueue;

    @SqsListener("${aws.sqs.request-queue}")
    public void onRequest(OrderQueryRequest req) {
        log.info("Recebido pedido de consulta para userId={} corr={}", req.userId(), req.correlationId());

        List<Order> orders = repository.findByUserId(req.userId());

        var summaries = orders.stream()
                .map(o -> new OrderQueryReply.OrderSummary(o.getOrderId(), o.getStatus()))
                .toList();

        var reply = new OrderQueryReply(req.correlationId(), summaries);
        String replyQueue = (req.replyToQueue() != null && !req.replyToQueue().isBlank())
                ? req.replyToQueue() : defaultReplyQueue;

        sqsTemplate.send(to -> to.queue(replyQueue).payload(reply));
        log.info("Resposta publicada em {} corr={}", replyQueue, req.correlationId());
    }
}

