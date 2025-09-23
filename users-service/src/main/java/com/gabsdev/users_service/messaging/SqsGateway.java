package com.gabsdev.users_service.messaging;

import com.gabsdev.contracts.dto.OrderQueryReply;
import com.gabsdev.contracts.dto.OrderQueryRequest;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsGateway {

    private final SqsTemplate sqsTemplate;

    // ⬇️ trocado de ConcurrentHashMap para Map (injeção por interface)
    private final Map<String, CompletableFuture<OrderQueryReply>> correlationMap;

    @Value("${aws.sqs.request-queue}")
    private String requestQueue;

    @Value("${aws.sqs.reply-queue}")
    private String replyQueue;

    @Value("${chatbot.reply-timeout-ms:3000}")
    private long replyTimeoutMs;

    public OrderQueryReply requestOrdersForUser(String userId) {
        String correlationId = UUID.randomUUID().toString();

        var future = new CompletableFuture<OrderQueryReply>();
        correlationMap.put(correlationId, future);

        var msg = new OrderQueryRequest(userId, correlationId, replyQueue);
        sqsTemplate.send(to -> to.queue(requestQueue).payload(msg));
        log.info("Enviado pedido de consulta: userId={} corr={}", userId, correlationId);

        try {
            return future.get(replyTimeoutMs, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.ACCEPTED,
                    "Consulta em processamento, tente novamente em instantes",
                    ex
            );
        } finally {
            correlationMap.remove(correlationId);
        }
    }

    @SqsListener("${aws.sqs.reply-queue}")
    public void onReply(OrderQueryReply reply) {
        var future = correlationMap.get(reply.correlationId());
        if (future != null) {
            future.complete(reply);
            log.info("Resposta correlacionada corr={} ({} pedidos)",
                    reply.correlationId(), reply.orders().size());
        } else {
            log.warn("Resposta órfã recebida corr={}", reply.correlationId());
        }
    }
}