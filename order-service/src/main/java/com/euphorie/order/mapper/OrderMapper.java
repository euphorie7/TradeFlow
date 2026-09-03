package com.euphorie.order.mapper;

import org.springframework.stereotype.Component;

import com.euphorie.order.dto.OrderResponseDto;
import com.euphorie.order.entity.Order;

@Component
public class OrderMapper {

    public OrderResponseDto toDto(Order order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .portfolioId(order.getPortfolioId())
                .symbol(order.getSymbol())
                .side(order.getSide())
                .quantity(order.getQuantity())
                .price(order.getPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}