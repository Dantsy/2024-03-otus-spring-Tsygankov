package ru.otus.spring.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw.models.Order;

import java.util.concurrent.ForkJoinPool;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderGeneratorServiceImpl implements OrderGeneratorService {

    private final OrderGateway orderGateway;
    private final OrderService orderService;

    @Override
    public void startGeneratingOrders() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.execute(() -> {
            Order newOrder = orderService.generateRandomOrder();
            log.info("New order generated: {}", newOrder);
            Order processedOrder = orderGateway.processOrder(newOrder);
            log.info("Order processed: {}", processedOrder);
        });
    }
}