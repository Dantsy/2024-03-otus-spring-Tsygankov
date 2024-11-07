package ru.otus.spring.hw.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import ru.otus.spring.hw.services.OrderService;

import static ru.otus.spring.hw.configuration.OrderIntegrationConfig.PREPARING_CHANNEL_NAME;

@Slf4j
@Configuration
public class OrderFactoryIntegrationConfig {

    public static final String ORDERS_CHANNEL_NAME = "orderFactoryOrdersChannel";

    @Bean
    public MessageChannelSpec<?, ?> ordersChannel() {
        return MessageChannels.queue(5);
    }

    @Bean
    public MessageChannelSpec<?, ?> orderConsumerChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public IntegrationFlow orderFactoryFlow(OrderService orderService) {
        return IntegrationFlow.from(ORDERS_CHANNEL_NAME)
                .handle(orderService, "processOrder")
                .split()
                .channel(PREPARING_CHANNEL_NAME)
                .get();
    }
}