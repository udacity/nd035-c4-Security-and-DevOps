package com.udacity.orders.api;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.udacity.orders.domain.Order;
import com.udacity.orders.domain.OrderStatus;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class OrderResourceAssembler implements ResourceAssembler<Order, Resource<Order>> {

    @Override
    public Resource<Order> toResource(Order order) {
        Resource<Order> orderResource = new Resource<>(order,
                linkTo(methodOn(OrderController.class).get(order.getId())).withSelfRel());

        if (order.getStatus() == OrderStatus.IN_PROGRESS) {
            orderResource.add(
                    linkTo(methodOn(OrderController.class)
                            .cancel(order.getId())).withRel("cancel"));
        }
        return orderResource;
    }
}
