package ru.otus.spring.hw.models;

import lombok.Builder;
import lombok.Data;
import ru.otus.spring.hw.models.order.MenuItemCategory;

@Builder
@Data
public class MenuItem {
    private Long id;
    private String name;
    private double price;
    private MenuItemCategory category;
}