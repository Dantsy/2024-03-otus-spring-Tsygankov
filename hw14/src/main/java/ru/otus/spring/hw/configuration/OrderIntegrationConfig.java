package ru.otus.spring.hw.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.handler.LoggingHandler;
import ru.otus.spring.hw.models.Order;
import ru.otus.spring.hw.models.order.OrderStatus;
import ru.otus.spring.hw.services.OrderService;

@Configuration
public class OrderIntegrationConfig {

    public static final String ORDERS_CHANNEL_NAME = "ordersChannel";
    public static final String PREPARING_CHANNEL_NAME = "preparingChannel";
    public static final String READY_CHANNEL_NAME = "readyChannel";
    public static final String DELIVERED_CHANNEL_NAME = "deliveredChannel";
    public static final String CANCELLED_CHANNEL_NAME = "cancelledChannel";

    @Bean
    public MessageChannelSpec<?, ?> ordersChannel() {
        return MessageChannels.queue(5);
    }

    @Bean
    public MessageChannelSpec<?, ?> preparingChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public MessageChannelSpec<?, ?> readyChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public MessageChannelSpec<?, ?> deliveredChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public MessageChannelSpec<?, ?> cancelledChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public IntegrationFlow orderProcessingFlow(OrderService orderService) {
        return IntegrationFlow.from(ORDERS_CHANNEL_NAME)
                .handle(orderService, "processOrder")
                .<Order, OrderStatus>route(order -> order.getStatus(),
                        mapping -> mapping
                                .subFlowMapping(OrderStatus.PREPARING, sf -> sf.channel(PREPARING_CHANNEL_NAME))
                                .subFlowMapping(OrderStatus.READY, sf -> sf.channel(READY_CHANNEL_NAME))
                                .subFlowMapping(OrderStatus.DELIVERED, sf -> sf.channel(DELIVERED_CHANNEL_NAME))
                                .subFlowMapping(OrderStatus.CANCELLED, sf -> sf.channel(CANCELLED_CHANNEL_NAME))
                )
                .get();
    }

    @Bean
    public IntegrationFlow preparingOrderFlow() {
        return IntegrationFlow.from(PREPARING_CHANNEL_NAME)
                .log(LoggingHandler.Level.INFO, "", message -> "Order is being prepared: %s".formatted(message.getPayload()))
                .get();
    }

    @Bean
    public IntegrationFlow readyOrderFlow() {
        return IntegrationFlow.from(READY_CHANNEL_NAME)
                .log(LoggingHandler.Level.INFO, "", message -> "Order is ready: %s".formatted(message.getPayload()))
                .get();
    }

    @Bean
    public IntegrationFlow deliveredOrderFlow() {
        return IntegrationFlow.from(DELIVERED_CHANNEL_NAME)
                .log(LoggingHandler.Level.INFO, "", message -> "Order is delivered: %s".formatted(message.getPayload()))
                .get();
    }

    @Bean
    public IntegrationFlow cancelledOrderFlow() {
        return IntegrationFlow.from(CANCELLED_CHANNEL_NAME)
                .log(LoggingHandler.Level.INFO, "", message -> "Order is cancelled: %s".formatted(message.getPayload()))
                .get();
    }
}