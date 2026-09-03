
package com.euphorie.order.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.euphorie.order.dto.CreateOrderDto;
import com.euphorie.order.dto.OrderResponseDto;
import com.euphorie.order.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponseDto create(
            @Valid @RequestBody CreateOrderDto dto,
            @AuthenticationPrincipal(expression = "claims['id']")
            Long userId) {

        return orderService.create(dto, userId);
    }

    @GetMapping
    public List<OrderResponseDto> findMyOrders(
            @AuthenticationPrincipal(expression = "claims['id']")
            Long userId) {

        return orderService.findByUserId(userId);
    }

    @GetMapping("/{id}")
    public OrderResponseDto findById(
            @PathVariable Long id,
            @AuthenticationPrincipal(expression = "claims['id']")
            Long userId) {

        return orderService.findById(id, userId);
    }
}