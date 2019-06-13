package com.udacity.orders.service;

import com.udacity.orders.domain.Order;
import com.udacity.orders.domain.OrderRepository;
import com.udacity.orders.domain.OrderStatus;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final SalesService salesService;

    public OrderService(OrderRepository repository, SalesService salesService) {
        this.repository = repository;
        this.salesService = salesService;
    }

    public Order findById(Long id) {
        return repository.findById(id).orElseThrow(OrderNotFoundException::new);
    }

    public Order place(Order order) {
        order.setStatus(OrderStatus.IN_PROGRESS);
        order = repository.save(order);
        salesService.send(order);
        return order;
    }

    public Order cancel(Long id) {
        Order order = findById(id);
        if (order.getStatus() != OrderStatus.IN_PROGRESS) {
            throw new CancelStatusException("You cannot cancel a order that is not in-progress");
        }
        order.setStatus(OrderStatus.CANCELLED);
        order = repository.save(order);
        salesService.send(order);
        return order;
    }
}
