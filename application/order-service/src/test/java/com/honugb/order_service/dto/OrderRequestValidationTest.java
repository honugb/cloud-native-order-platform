package com.honugb.order_service.dto;

import java.math.BigDecimal;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderRequestValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        factory.close();
    }

    @Test
    void shouldAcceptValidOrder() {

        OrderRequest request = new OrderRequest(
            "Laptop",
            2,
            new BigDecimal("45000.00")
        );

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void shouldRejectBlankProductName() {

        OrderRequest request = new OrderRequest(
            "",
            2,
            new BigDecimal("45000.00")
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldRejectZeroQuantity() {

        OrderRequest request = new OrderRequest(
            "Laptop",
            0,
            new BigDecimal("45000.00")
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldRejectNegativePrice() {

        OrderRequest request = new OrderRequest(
            "Laptop",
            2,
            new BigDecimal("-100.00")
        );

        assertFalse(validator.validate(request).isEmpty());
    }
}