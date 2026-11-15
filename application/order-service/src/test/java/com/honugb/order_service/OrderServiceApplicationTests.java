package com.honugb.order_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "spring.mongodb.uri=mongodb://localhost:27017/orderdb"
})
class OrderServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}