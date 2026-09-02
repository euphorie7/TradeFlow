package com.euphorie.position.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.euphorie.position.entity.Position;
import com.euphorie.portfolio.service.PortfolioService;
import com.euphorie.position.repository.PositionRepository;

import com.euphorie.portfolio.entity.Portfolio;
import com.euphorie.portfolio.repository.PortfolioRepository;

import org.springframework.web.server.ResponseStatusException;


@Service
public class PositionService {

    private final PositionRepository positionRepository;
    private final PortfolioRepository portfolioRepository;

    public PositionService(
            PositionRepository positionRepository,
            PortfolioRepository portfolioRepository) {

        this.positionRepository = positionRepository;
        this.portfolioRepository = portfolioRepository;
    }

    public List<Position> getPositionsByPortfolioId(Long portfolioId , Long userId) {

        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Portfolio introuvable"
                ));

        if (!portfolio.getUserId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Accès interdit"
            );
        }

        return positionRepository.findByPortfolioId(portfolioId);
    }
    
   
}