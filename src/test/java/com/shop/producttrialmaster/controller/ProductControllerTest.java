package com.shop.producttrialmaster.controller;

import tools.jackson.databind.ObjectMapper;
import com.shop.producttrialmaster.entity.Product;
import com.shop.producttrialmaster.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Context Spring complet + vraie base H2 : pas de mock, on teste le comportement réel de bout en bout
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

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
    void findAll_returnsListOfProducts() throws Exception {
        productRepository.save(validProduct().build());

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Chaise"));
    }

    @Test
    void findById_returnsProduct() throws Exception {
        Product saved = productRepository.save(validProduct().build());

        mockMvc.perform(get("/products/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Chaise"));
    }

    @Test
    void findById_returns404_whenProductDoesNotExist() throws Exception {
        mockMvc.perform(get("/products/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_returns201_whenProductIsValid() throws Exception {
        Product product = validProduct().build();

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Chaise"));
    }

    @Test
    void create_returns400_whenNameIsMissing() throws Exception {
        Product invalid = validProduct().name(null).build();

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_returns204() throws Exception {
        Product saved = productRepository.save(validProduct().build());

        mockMvc.perform(delete("/products/" + saved.getId()))
                .andExpect(status().isNoContent());
    }
}
