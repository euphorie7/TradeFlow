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
import com.euphorie.order.kafka.event.OrderExecutedEvent;
import com.euphorie.order.kafka.producer.OrderEventProducer;


//events 
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.context.ApplicationEventPublisher;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderEventProducer orderEventProducer;
    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderService(
            OrderRepository orderRepository,
            OrderMapper orderMapper,
            OrderEventProducer orderEventProducer,
            ApplicationEventPublisher applicationEventPublisher) {

        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderEventProducer = orderEventProducer;
        this.applicationEventPublisher = applicationEventPublisher;
    }
    @Transactional
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

        order.setStatus(OrderStatus.EXECUTED);
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        OrderExecutedEvent event = new OrderExecutedEvent(
            savedOrder.getId(),
            savedOrder.getPortfolioId(),
            savedOrder.getSymbol(),
            savedOrder.getSide().name(),
            savedOrder.getQuantity(),
            savedOrder.getPrice()
        );

        
        OrderResponseDto orderResponse =  orderMapper.toDto(savedOrder);
        applicationEventPublisher.publishEvent(event);

        return orderResponse;
    }

    //@Async si je veux que ce handler soit repris par un autre thread
    @TransactionalEventListener(
        phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleEvent(OrderExecutedEvent event) {

        orderEventProducer.sendOrderExecuted(event);

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