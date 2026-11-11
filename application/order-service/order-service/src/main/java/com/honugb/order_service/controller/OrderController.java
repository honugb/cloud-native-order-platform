package com.honugb.order_service.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @GetMapping("/status")
    public Map<String, String> status() {
        return Map.of(
            "application", "Cloud Native Order Platform",
            "status", "RUNNING",
            "version", "1.0.0"
        );
    }
}