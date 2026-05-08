package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
@SpringBootApplication
public class Demo8Application {
    private static final Logger logger = LoggerFactory.getLogger(Demo8Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Demo8Application.class, args);
    }
}
