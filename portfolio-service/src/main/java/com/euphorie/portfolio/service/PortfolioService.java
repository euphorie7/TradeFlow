package com.euphorie.portfolio.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.euphorie.portfolio.entity.Portfolio;
import com.euphorie.portfolio.repository.PortfolioRepository;
import com.euphorie.portfolio.mapper.PortfolioMapper;
import com.euphorie.portfolio.dto.CreatePortfolioDto;
import com.euphorie.portfolio.dto.PortfolioResponseDto;


import org.springframework.web.server.ResponseStatusException;


@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final PortfolioMapper portfolioMapper;
    public PortfolioService(PortfolioRepository portfolioRepository,
                            PortfolioMapper portfolioMapper
    ) {
        this.portfolioRepository = portfolioRepository;
        this.portfolioMapper = portfolioMapper;
    }

    public PortfolioResponseDto create(CreatePortfolioDto dto, Long userId) { 
        Portfolio portfolio = new Portfolio();
        portfolio.setName(dto.getName());
        portfolio.setCashBalance(dto.getCashBalance() != null 
                                ? dto.getCashBalance()
                                : BigDecimal.ZERO);
        portfolio.setUserId(userId);


        Portfolio saved = portfolioRepository.save(portfolio);

        return portfolioMapper.toDto(saved);
    }

    public List<PortfolioResponseDto> findByUserId(Long userId) {

        return portfolioRepository.findByUserId(userId)
                .stream()
                .map(pf -> portfolioMapper.toDto(pf))
                .toList();
    }


    public PortfolioResponseDto findById(Long portfolioId ,Long userId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Portfolio introuvable"));
        
        if (!portfolio.getUserId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Accès interdit"
            );
        }

        return portfolioMapper.toDto(portfolio);
    } 
    
   
}