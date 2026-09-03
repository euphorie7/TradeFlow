package com.euphorie.order.dto;

import java.math.BigDecimal;

import com.euphorie.order.entity.OrderSide;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateOrderDto {

    @NotNull
    private Long portfolioId;

    @NotBlank
    private String symbol;

    @NotNull
    private OrderSide side;

    @NotNull
    @Positive
    private BigDecimal quantity;

    @NotNull
    @Positive
    private BigDecimal price;
}