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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
    void getWishlist_returnsEmptyWishlist_whenNoneExistsYet() throws Exception {
        String token = createUserAndGetToken("user1@example.com");

        mockMvc.perform(get("/wishlist").header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isEmpty());
    }

    @Test
    void addProduct_addsProductToWishlist() throws Exception {
        String token = createUserAndGetToken("user2@example.com");
        Long productId = createProduct();

        mockMvc.perform(post("/wishlist/products/" + productId).header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products[0].id").value(productId));
    }

    @Test
    void removeProduct_removesProductFromWishlist() throws Exception {
        String token = createUserAndGetToken("user3@example.com");
        Long productId = createProduct();

        mockMvc.perform(post("/wishlist/products/" + productId).header(HttpHeaders.AUTHORIZATION, token));

        mockMvc.perform(delete("/wishlist/products/" + productId).header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isEmpty());
    }

    @Test
    void getWishlist_returns401_whenNoToken() throws Exception {
        mockMvc.perform(get("/wishlist"))
                .andExpect(status().isUnauthorized());
    }
}
