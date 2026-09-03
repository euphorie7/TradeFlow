package com.euphorie.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.euphorie.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    List<Order> findByPortfolioId(Long portfolioId);
}