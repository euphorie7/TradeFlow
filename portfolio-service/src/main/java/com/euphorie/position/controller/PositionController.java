package com.euphorie.position.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.euphorie.position.dto.PositionResponseDto;
import com.euphorie.position.entity.Position;
import com.euphorie.position.service.PositionService;

@RestController
@RequestMapping("/positions")
public class PositionController {

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping("/portfolio/{portfolioId}")
    public List<PositionResponseDto> getPositionsByPortfolioId(
            @PathVariable Long portfolioId,
            @AuthenticationPrincipal(expression = "claims['id']") Long userId) {

        return positionService.getPositionsByPortfolioId(
                portfolioId,
                userId
        );
    }
}