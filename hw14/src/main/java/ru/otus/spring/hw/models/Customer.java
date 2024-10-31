package ru.otus.spring.hw.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Customer {
    private Long id;
    private String name;
    private String phoneNumber;
}