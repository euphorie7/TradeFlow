package com.euphorie.portfolio.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.euphorie.portfolio.dto.CreatePortfolioDto;
import com.euphorie.portfolio.dto.PortfolioResponseDto;
import com.euphorie.portfolio.service.PortfolioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @PostMapping
    public PortfolioResponseDto create(
        @Valid @RequestBody CreatePortfolioDto dto,
        @AuthenticationPrincipal(expression = "claims['id']") Long id
    ) {
        return portfolioService.create(dto, id);
    }

    @GetMapping
    public List<PortfolioResponseDto> findMyPortfolios(
            @AuthenticationPrincipal(expression = "claims['id']")
            Long userId) {

        return portfolioService.findByUserId(userId);
    }

    @GetMapping("/{id}")
    public PortfolioResponseDto findById(
            @PathVariable Long id,
            @AuthenticationPrincipal(expression = "claims['id']")
            Long userId) {

        return portfolioService.findById(id, userId);
    }

    

}