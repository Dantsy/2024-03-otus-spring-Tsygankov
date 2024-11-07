package ru.otus.spring.hw.services;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

import static ru.otus.spring.hw.configuration.OrderIntegrationConfig.*;

@Service
public class ReportService {

    private int totalOrders;
    private int readyOrders;
    private int cancelledOrders;

    @ServiceActivator(inputChannel = ORDERS_CHANNEL_NAME)
    public void totalOrdersCounter() {
        totalOrders++;
    }

    @ServiceActivator(inputChannel = READY_CHANNEL_NAME)
    public void readyOrdersCounter() {
        readyOrders++;
    }

    @ServiceActivator(inputChannel = CANCELLED_CHANNEL_NAME)
    public void cancelledOrdersCounter() {
        cancelledOrders++;
    }

    @ServiceActivator(inputChannel = DELIVERED_CHANNEL_NAME)
    public void showReport() {
        System.out.printf("Ready orders: %d%n", readyOrders);
        System.out.printf("Cancelled orders: %d%n", cancelledOrders);
        System.out.printf("Total orders: %d%n", totalOrders);
    }
}