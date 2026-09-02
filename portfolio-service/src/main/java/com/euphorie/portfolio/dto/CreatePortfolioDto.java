package com.euphorie.portfolio.dto;

import lombok.Data;
import java.math.BigDecimal;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Data 
public class CreatePortfolioDto {

    @NotBlank
    private String name;

    
    @PositiveOrZero
    private BigDecimal cashBalance;
    
}