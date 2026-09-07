package com.euphorie.order.kafka.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
@Data
@AllArgsConstructor
public class OrderExecutedEvent {

    private Long orderId;
    private Long portfolioId;
    private String symbol;
    private String side;
    private BigDecimal quantity;
    private BigDecimal price;

}