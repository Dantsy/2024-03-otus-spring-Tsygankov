package ru.otus.spring.hw.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.handler.LoggingHandler;
import ru.otus.spring.hw.models.Order;
import ru.otus.spring.hw.models.order.OrderStatus;

@Configuration
public class OrderQualityControlIntegrationConfig {

    public static final String QUALITY_CONTROL_CHANNEL_NAME = "qualityControlChannel";
    public static final String DEFECTIVE_ORDERS_CHANNEL_NAME = "defectiveOrdersChannel";
    public static final String NON_DEFECTIVE_ORDERS_CHANNEL_NAME = "nonDefectiveOrdersChannel";

    @Bean
    public MessageChannelSpec<?, ?> qualityControlChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public MessageChannelSpec<?, ?> nonDefectiveOrdersChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public MessageChannelSpec<?, ?> defectiveOrdersChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    IntegrationFlow orderQualityControlFlow() {
        return IntegrationFlow.from(QUALITY_CONTROL_CHANNEL_NAME)
                .<Order, String>route(order -> {
                    if (order.getStatus() == OrderStatus.CANCELLED) {
                        return DEFECTIVE_ORDERS_CHANNEL_NAME;
                    } else {
                        return NON_DEFECTIVE_ORDERS_CHANNEL_NAME;
                    }
                })
                .get();
    }

    @Bean
    IntegrationFlow fixingDefectiveOrderFlow() {
        return IntegrationFlow.from(DEFECTIVE_ORDERS_CHANNEL_NAME)
                .<Order>handle((payload, headers) -> {
                    payload.setStatus(OrderStatus.PREPARING);
                    return payload;
                })
                .log(LoggingHandler.Level.INFO, "", message -> "Defective order fixed: %s".formatted(message.getPayload()))
                .channel(NON_DEFECTIVE_ORDERS_CHANNEL_NAME)
                .get();
    }
}