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
        User user = new User();
        user.setUsername("jdoe");
        user.setFirstname("John");
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("secret123"));
        userRepository.save(user);
        return "Bearer " + jwtUtil.generateToken(email);
    }

    private Long createProduct() {
        Product product = new Product();
        product.setCode("C001");
        product.setName("Chaise");
        product.setDescription("Une chaise");
        product.setCategory("Mobilier");
        product.setPrice(49.99);
        product.setQuantity(10);
        product.setInternalReference("REF001");
        productRepository.save(product);
        return product.getId();
    }

    /**
     * Liste vide créée automatiquement au premier accès.
     * Empty wishlist auto-created on first access.
     */
    @Test
    void getWishlist_returnsEmptyWishlist_whenNoneExistsYet() throws Exception {
        String token = createUserAndGetToken("user1@example.com");

        mockMvc.perform(get("/wishlist").header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isEmpty());
    }

    /**
     * Ajoute un produit à la liste d'envie.
     * Adds a product to the wishlist.
     */
    @Test
    void addProduct_addsProductToWishlist() throws Exception {
        String token = createUserAndGetToken("user2@example.com");
        Long productId = createProduct();

        mockMvc.perform(post("/wishlist/products/" + productId).header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products[0].id").value(productId));
    }

    /**
     * Retire un produit de la liste d'envie.
     * Removes a product from the wishlist.
     */
    @Test
    void removeProduct_removesProductFromWishlist() throws Exception {
        String token = createUserAndGetToken("user3@example.com");
        Long productId = createProduct();

        mockMvc.perform(post("/wishlist/products/" + productId).header(HttpHeaders.AUTHORIZATION, token));

        mockMvc.perform(delete("/wishlist/products/" + productId).header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isEmpty());
    }

    /**
     * Sans token, accès refusé.
     * Without a token, access is denied.
     */
    @Test
    void getWishlist_returns401_whenNoToken() throws Exception {
        mockMvc.perform(get("/wishlist"))
                .andExpect(status().isUnauthorized());
    }
}
