package ru.otus.spring.hw.services;

import org.springframework.stereotype.Service;
import ru.otus.spring.hw.models.Customer;
import ru.otus.spring.hw.models.MenuItem;
import ru.otus.spring.hw.models.order.MenuItemCategory;
import ru.otus.spring.hw.models.Order;
import ru.otus.spring.hw.models.order.OrderStatus;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public Order processOrder(Order order) {
        try {
            if (Math.random() > 0.1) {
                order.setStatus(OrderStatus.PREPARING);
            } else {
                order.setStatus(OrderStatus.CANCELLED);
            }
        } catch (Exception e) {
            System.err.println("Error processing order: " + e.getMessage());
            order.setStatus(OrderStatus.CANCELLED);
        }
        return order;
    }

    @Override
    public Order generateRandomOrder() {
        List<MenuItem> items = IntStream.range(1, new Random().nextInt(5) + 1)
                .mapToObj(i -> generateRandomMenuItem())
                .collect(Collectors.toList());

        return Order.builder()
                .customer(generateRandomCustomer())
                .items(items)
                .status(OrderStatus.PENDING)
                .build();
    }

    private MenuItem generateRandomMenuItem() {
        Random random = new Random();
        MenuItemCategory[] categories = MenuItemCategory.values();
        MenuItemCategory category = categories[random.nextInt(categories.length)];

        return MenuItem.builder()
                .name("Item " + random.nextInt(100))
                .price(random.nextDouble() * 20)
                .category(category)
                .build();
    }

    private Customer generateRandomCustomer() {
        Random random = new Random();
        return Customer.builder()
                .name("Customer " + random.nextInt(100))
                .phoneNumber("1234567890")
                .build();
    }
}