package com.udacity.orders.api;

import com.udacity.orders.domain.Order;
import com.udacity.orders.service.OrderService;
import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderResourceAssembler assembler;

    public OrderController(OrderService orderService,
            OrderResourceAssembler assembler) {
        this.orderService = orderService;
        this.assembler = assembler;
    }

    @GetMapping("/{id}")
    Resource<?> get(@PathVariable Long id) {
        Order order = orderService.findById(id);
        return assembler.toResource(order);
    }

    @PostMapping
    ResponseEntity<?> place(@Valid @RequestBody Order order) throws URISyntaxException {
        order = orderService.place(order);
        Resource<Order> resource = assembler.toResource(order);
        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> cancel(@PathVariable Long id) {
        Order order = orderService.cancel(id);
        return ResponseEntity.ok(assembler.toResource(order));
    }
}
