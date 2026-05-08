package com.example.demo;

import com.example.demo.service.BuildService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

public class Run implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(Run.class);
    private final BuildService orderService;

    public Run(BuildService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void run(String... args) throws Exception {

    }

    /*private void step_1() {
        Order order1 = new Order();
        order1.setName("Нарисовать член");
        order1.setDescription("Большой");
        order1.setPrice(666);
        Order created1 = orderService.createOrder(order1, user);
        logger.info("Создана задача: Опись={}", created1.getDescription());
    }

    @Override
    public void run(String... args) throws Exception {
        step_1();
    }*/
}
