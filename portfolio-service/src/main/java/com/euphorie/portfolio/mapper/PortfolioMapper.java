package com.euphorie.portfolio.mapper;

import org.springframework.stereotype.Component;
import com.euphorie.portfolio.dto.PortfolioResponseDto;
import com.euphorie.portfolio.entity.Portfolio;

@Component
public class PortfolioMapper {

    public PortfolioResponseDto toDto(Portfolio portfolio) {

        return PortfolioResponseDto.builder()
                .id(portfolio.getId())
                .userId(portfolio.getUserId())
                .name(portfolio.getName())
                .cashBalance(portfolio.getCashBalance())
                .build();
    }
}