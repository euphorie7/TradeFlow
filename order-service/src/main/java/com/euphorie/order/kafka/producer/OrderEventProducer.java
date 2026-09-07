package com.euphorie.order.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import com.euphorie.order.kafka.event.OrderExecutedEvent;


@Service
public class OrderEventProducer { 


    private final KafkaTemplate<Object, Object> kafkaTemplate;

    public OrderEventProducer(
            KafkaTemplate<Object, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderExecuted(OrderExecutedEvent event) {
        System.out.println("PUBLISH KAFKA: " + event);

        kafkaTemplate.send(
                "order-executed",
                event.getPortfolioId().toString(),
                event
        );
    }
}