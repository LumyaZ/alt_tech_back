package com.shop.producttrialmaster.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Product.ProductBuilder validProduct() {
        return Product.builder()
                .code("C001")
                .name("Chaise")
                .description("Une chaise")
                .category("Mobilier")
                .price(49.99)
                .quantity(10)
                .internalReference("REF001");
    }

    @Test
    void isValid_whenAllRequiredFieldsAreFilled() {
        Set<ConstraintViolation<Product>> violations = validator.validate(validProduct().build());

        assertThat(violations).isEmpty();
    }

    @Test
    void violatesConstraint_whenNameIsBlank() {
        Product product = validProduct().name("").build();

        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void violatesConstraint_whenDescriptionExceedsMaxSize() {
        Product product = validProduct().description("a".repeat(301)).build();

        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("description"));
    }

    @Test
    void onCreate_setsCreatedAt() {
        Product product = validProduct().build();

        product.onCreate();

        assertThat(product.getCreatedAt()).isNotNull();
    }

    @Test
    void onUpdate_setsUpdatedAt() {
        Product product = validProduct().build();

        product.onUpdate();

        assertThat(product.getUpdatedAt()).isNotNull();
    }
}
