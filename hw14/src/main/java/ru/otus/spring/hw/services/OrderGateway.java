package ru.otus.spring.hw.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.spring.hw.models.Order;

import static ru.otus.spring.hw.configuration.OrderIntegrationConfig.ORDERS_CHANNEL_NAME;
import static ru.otus.spring.hw.configuration.OrderIntegrationConfig.DELIVERED_CHANNEL_NAME;

@MessagingGateway
public interface OrderGateway {

    @Gateway(requestChannel = ORDERS_CHANNEL_NAME, replyChannel = DELIVERED_CHANNEL_NAME)
    Order processOrder(Order order);

}