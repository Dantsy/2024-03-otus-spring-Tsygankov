package ru.otus.spring.hw.models;

import lombok.Builder;
import lombok.Data;
import ru.otus.spring.hw.models.order.OrderStatus;

import java.util.List;
import java.util.UUID;

@Builder
@Data
public class Order {
    private UUID id;
    private Customer customer;
    private List<MenuItem> items;
    private OrderStatus status;

    public Order() {
        this.id = UUID.randomUUID();
    }

    public Order(UUID id, Customer customer, List<MenuItem> items, OrderStatus status) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.customer = customer;
        this.items = items;
        this.status = status;
    }

    public static OrderBuilder builder() {
        return new OrderBuilder().id(UUID.randomUUID());
    }

    public static class OrderBuilder {

        public OrderBuilder id(UUID id) {
            return this;
        }
    }
}