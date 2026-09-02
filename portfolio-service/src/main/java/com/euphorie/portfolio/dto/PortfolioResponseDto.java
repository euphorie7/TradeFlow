package com.euphorie.portfolio.dto;

import lombok.Data;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;


@Data 
@Builder
public class PortfolioResponseDto {

    @NotNull
    private Long id;
    @NotNull
    private Long userId;
    @NotBlank
    private String name;

    @NotNull
    @PositiveOrZero
    private BigDecimal cashBalance;
    
}