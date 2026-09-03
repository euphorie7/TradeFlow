package com.euphorie.order.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.euphorie.order.dto.CreateOrderDto;
import com.euphorie.order.dto.OrderResponseDto;
import com.euphorie.order.entity.Order;
import com.euphorie.order.entity.OrderStatus;
import com.euphorie.order.mapper.OrderMapper;
import com.euphorie.order.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(
            OrderRepository orderRepository,
            OrderMapper orderMapper) {

        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public OrderResponseDto create(
            CreateOrderDto dto,
            Long userId) {

        Order order = new Order();

        order.setUserId(userId);
        order.setPortfolioId(dto.getPortfolioId());
        order.setSymbol(dto.getSymbol());
        order.setSide(dto.getSide());
        order.setQuantity(dto.getQuantity());
        order.setPrice(dto.getPrice());

        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        return orderMapper.toDto(savedOrder);
    }

    public List<OrderResponseDto> findByUserId(Long userId) {

        return orderRepository.findByUserId(userId)
                .stream()
                .map(o -> orderMapper.toDto(o))
                .toList();
    }

    public OrderResponseDto findById(
            Long orderId,
            Long userId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Ordre introuvable"
                ));

        if (!order.getUserId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Accès interdit"
            );
        }

        return orderMapper.toDto(order);
    }
}