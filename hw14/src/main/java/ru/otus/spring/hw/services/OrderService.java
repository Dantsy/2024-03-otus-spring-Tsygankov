package ru.otus.spring.hw.services;

import ru.otus.spring.hw.models.Order;

public interface OrderService {
    Order processOrder(Order order);

    Order generateRandomOrder();
}