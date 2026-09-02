package com.euphorie.position.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.euphorie.position.entity.Position;
import java.util.List;

import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position,Long> {
    List<Position> findByPortfolioId(Long portfolioId);

}