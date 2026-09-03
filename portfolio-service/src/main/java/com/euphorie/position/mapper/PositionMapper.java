package com.euphorie.position.mapper;

import org.springframework.stereotype.Component;
import com.euphorie.position.dto.PositionResponseDto;
import com.euphorie.position.entity.Position;



@Component
public class PositionMapper {

    public PositionResponseDto toDto(Position position) {
        return PositionResponseDto.builder()
                .id(position.getId())
                .portfolioId(position.getPortfolio().getId())
                .symbol(position.getSymbol())
                .quantity(position.getQuantity())
                .averagePrice(position.getAveragePrice())
                .build();
    }
}