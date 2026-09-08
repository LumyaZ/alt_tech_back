package com.shop.producttrialmaster.controller;

import com.shop.producttrialmaster.entity.Product;
import com.shop.producttrialmaster.entity.User;
import com.shop.producttrialmaster.repository.ProductRepository;
import com.shop.producttrialmaster.repository.UserRepository;
import com.shop.producttrialmaster.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    private String createUserAndGetToken(String email) {
        userRepository.save(User.builder()
                .username("jdoe")
                .firstname("John")
                .email(email)
                .password(passwordEncoder.encode("secret123"))
                .build());
        return "Bearer " + jwtUtil.generateToken(email);
    }

    private Long createProduct() {
        Product product = productRepository.save(Product.builder()
                .code("C001")
                .name("Chaise")
                .description("Une chaise")
                .category("Mobilier")
                .price(49.99)
                .quantity(10)
                .internalReference("REF001")
                .build());
        return product.getId();
    }

    @Test
    void getCart_returnsEmptyCart_whenNoneExistsYet() throws Exception {
        String token = createUserAndGetToken("user1@example.com");

        mockMvc.perform(get("/cart").header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isEmpty());
    }

    @Test
    void addItem_addsProductToCart() throws Exception {
        String token = createUserAndGetToken("user2@example.com");
        Long productId = createProduct();

        mockMvc.perform(post("/cart/items")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("productId", productId, "quantity", 2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].product.id").value(productId))
                .andExpect(jsonPath("$.items[0].quantity").value(2));
    }

    @Test
    void addItem_returns400_whenProductIdIsMissing() throws Exception {
        String token = createUserAndGetToken("user3@example.com");

        mockMvc.perform(post("/cart/items")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("quantity", 2))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void removeItem_removesProductFromCart() throws Exception {
        String token = createUserAndGetToken("user4@example.com");
        Long productId = createProduct();

        mockMvc.perform(post("/cart/items")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("productId", productId, "quantity", 1))));

        mockMvc.perform(delete("/cart/items/" + productId).header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isEmpty());
    }

    @Test
    void getCart_returns401_whenNoToken() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().isUnauthorized());
    }
}
