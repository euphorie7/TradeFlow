package com.euphorie.kafka.consumer;

import org.springframework.stereotype.Component;
import org.springframework.kafka.annotation.KafkaListener;
import com.euphorie.kafka.event.OrderExecutedEvent;
import com.euphorie.position.service.PositionService;

@Component
public class OrderExecutedConsumer {

    private final PositionService positionService;

    public OrderExecutedConsumer(PositionService positionService) {
        this.positionService = positionService;
    }

    @KafkaListener(
        topics = "order-executed",
        groupId = "portfolio-service"
    )
    public void consume(OrderExecutedEvent event) {
        System.out.println("EVENT RECU PAR PORTFOLIO: " + event);
        if ("BUY".equals(event.getSide())) {

            positionService.applyBuy(
                event.getPortfolioId(),
                event.getSymbol(),
                event.getQuantity(),
                event.getPrice()
            );

        } else if ("SELL".equals(event.getSide())) {

            positionService.applySell(
                event.getPortfolioId(),
                event.getSymbol(),
                event.getQuantity(),
                event.getPrice()
            );
        }

    }

}