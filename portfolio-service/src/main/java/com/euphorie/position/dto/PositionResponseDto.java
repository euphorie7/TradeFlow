package com.euphorie.position.dto;
import java.math.BigDecimal;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class PositionResponseDto {

    private Long id;
    private Long portfolioId;
    private String symbol;
    private BigDecimal quantity;
    private BigDecimal averagePrice;

}