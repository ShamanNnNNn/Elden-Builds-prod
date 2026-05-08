package com.example.demo.controller;

public class OrderNotFoundExeption extends RuntimeException {
    public OrderNotFoundExeption(Long id) {
        super("Задача с ID " + id + " не найдена");
    }
}
