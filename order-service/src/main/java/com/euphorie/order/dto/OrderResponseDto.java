package com.euphorie.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.euphorie.order.entity.OrderSide;
import com.euphorie.order.entity.OrderStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponseDto {

    private Long id;

    private Long userId;

    private Long portfolioId;

    private String symbol;

    private OrderSide side;

    private BigDecimal quantity;

    private BigDecimal price;

    private OrderStatus status;

    private LocalDateTime createdAt;
}