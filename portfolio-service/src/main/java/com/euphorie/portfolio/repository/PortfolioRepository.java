package com.euphorie.portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.euphorie.portfolio.entity.Portfolio;
import java.util.List;

import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio,Long> {
    List<Portfolio> findByUserId(Long userId);

}