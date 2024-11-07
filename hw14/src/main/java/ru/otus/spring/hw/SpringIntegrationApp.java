package ru.otus.spring.hw;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.integration.annotation.IntegrationComponentScan;
import ru.otus.spring.hw.services.OrderGeneratorService;

@SpringBootApplication
@IntegrationComponentScan
@RequiredArgsConstructor
public class SpringIntegrationApp {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(SpringIntegrationApp.class, args);
		OrderGeneratorService orderService = ctx.getBean(OrderGeneratorService.class);
		orderService.startGeneratingOrders();
	}
}