package com.euphorie.position.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.euphorie.position.entity.Position;
import com.euphorie.portfolio.service.PortfolioService;
import com.euphorie.position.repository.PositionRepository;
import com.euphorie.position.mapper.PositionMapper;

import com.euphorie.portfolio.entity.Portfolio;
import com.euphorie.portfolio.service.PortfolioService;
import com.euphorie.portfolio.repository.PortfolioRepository;
import com.euphorie.portfolio.dto.PortfolioResponseDto;
import com.euphorie.position.dto.PositionResponseDto;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

@Service
public class PositionService {

    private final PositionRepository positionRepository;
    private final PortfolioService portfolioService;
    private final PositionMapper positionMapper;
    private final PortfolioRepository portfolioRepository;

    public PositionService(
            PositionRepository positionRepository,
            PortfolioService portfolioService,
            PositionMapper positionMapper,
            PortfolioRepository portfolioRepository) {

        this.positionRepository = positionRepository;
        this.portfolioService = portfolioService;
        this.positionMapper = positionMapper;
        this.portfolioRepository =portfolioRepository;
    }

    public List<PositionResponseDto> getPositionsByPortfolioId(Long portfolioId , Long userId) {

        // Portfolio portfolio = portfolioRepository.findById(portfolioId)
        //         .orElseThrow(() -> new ResponseStatusException(
        //                 HttpStatus.NOT_FOUND,
        //                 "Portfolio introuvable"
        //         ));

        // if (!portfolio.getUserId().equals(userId)) {
        //     throw new ResponseStatusException(
        //             HttpStatus.FORBIDDEN,
        //             "Accès interdit"
        //     );
        // }

        portfolioService.findById(portfolioId, userId);

        return positionRepository.findByPortfolioId(portfolioId)
                                .stream()
                                .map(p -> positionMapper.toDto(p))
                                .toList();
    }

    @Transactional
    public PositionResponseDto applyBuy(
            Long portfolioId,
            String symbol,
            BigDecimal quantity,
            BigDecimal price) {

        // 1. Récupérer le portfolio
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Portfolio introuvable"
                ));

        // 2. Calculer le coût de l'achat
        BigDecimal buyAmount = quantity.multiply(price);

        // 3. Vérifier que le portfolio a assez d'argent
        if (portfolio.getCashBalance().compareTo(buyAmount) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Solde insuffisant"
            );
        }

        // 4. Débiter le portfolio
        portfolio.setCashBalance(
                portfolio.getCashBalance().subtract(buyAmount)
        );

        portfolioRepository.save(portfolio);

        // 5. Chercher si la position existe déjà
        Optional<Position> existingPosition =
                positionRepository.findByPortfolioIdAndSymbol(
                        portfolioId,
                        symbol
                );

        if (existingPosition.isPresent()) {

            Position position = existingPosition.get();

            BigDecimal oldQuantity = position.getQuantity();
            BigDecimal oldAveragePrice = position.getAveragePrice();

            BigDecimal newQuantity =
                    oldQuantity.add(quantity);

            BigDecimal newAveragePrice =
                    oldAveragePrice.multiply(oldQuantity)
                            .add(price.multiply(quantity))
                            .divide(newQuantity);

            position.setQuantity(newQuantity);
            position.setAveragePrice(newAveragePrice);

            return positionMapper.toDto(
                    positionRepository.save(position)
            );
        }

        // 6. Sinon créer une nouvelle position
        Position position = new Position();

        position.setPortfolio(portfolio);
        position.setSymbol(symbol);
        position.setQuantity(quantity);
        position.setAveragePrice(price);

        return positionMapper.toDto(
                positionRepository.save(position)
        );
    }

    @Transactional
    public PositionResponseDto applySell(
            Long portfolioId,
            String symbol,
            BigDecimal quantity,
            BigDecimal price) {

        // 1. Récupérer le portfolio
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Portfolio introuvable"
                ));

        // 2. Récupérer la position
        Position position = positionRepository
                .findByPortfolioIdAndSymbol(portfolioId, symbol)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Position inexistante"
                ));

        // 3. Vérifier qu'on possède assez d'actions
        if (position.getQuantity().compareTo(quantity) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Quantité insuffisante"
            );
        }

        // 4. Calculer le montant de la vente
        BigDecimal sellAmount = quantity.multiply(price);

        // 5. Créditer le portfolio
        portfolio.setCashBalance(
                portfolio.getCashBalance().add(sellAmount)
        );

        portfolioRepository.save(portfolio);

        // 6. Diminuer la position
        BigDecimal newQuantity =
                position.getQuantity().subtract(quantity);

        // 7. Si on a tout vendu, supprimer la position
        if (newQuantity.compareTo(BigDecimal.ZERO) == 0) {
            positionRepository.delete(position);
            return null;
        }

        // 8. Sinon mettre à jour la quantité
        position.setQuantity(newQuantity);

        return positionMapper.toDto(
                positionRepository.save(position)
        );
    }
            
   
}