package com.shop.producttrialmaster.controller;

import tools.jackson.databind.ObjectMapper;
import com.shop.producttrialmaster.entity.Product;
import com.shop.producttrialmaster.repository.ProductRepository;
import com.shop.producttrialmaster.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Autowired
    private JwtUtil jwtUtil;

    private String bearerToken() {
        return "Bearer " + jwtUtil.generateToken("user@example.com");
    }

    private String adminBearerToken() {
        return "Bearer " + jwtUtil.generateToken("admin@admin.com");
    }

    private Product validProduct() {
        Product product = new Product();
        product.setCode("C001");
        product.setName("Chaise");
        product.setDescription("Une chaise");
        product.setCategory("Mobilier");
        product.setPrice(49.99);
        product.setQuantity(10);
        product.setInternalReference("REF001");
        return product;
    }

    /**
     * Liste les produits pour un utilisateur authentifié.
     * Lists products for an authenticated user.
     */
    @Test
    void findAll_returnsListOfProducts() throws Exception {
        productRepository.save(validProduct());

        mockMvc.perform(get("/products").header(HttpHeaders.AUTHORIZATION, bearerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Chaise"));
    }

    /**
     * Sans token, accès refusé.
     * Without a token, access is denied.
     */
    @Test
    void findAll_returns401_whenNoTokenProvided() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Récupère un produit par son id.
     * Retrieves a product by its id.
     */
    @Test
    void findById_returnsProduct() throws Exception {
        Product saved = productRepository.save(validProduct());

        mockMvc.perform(get("/products/" + saved.getId()).header(HttpHeaders.AUTHORIZATION, bearerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Chaise"));
    }

    /**
     * Produit inexistant -> 404.
     * Nonexistent product -> 404.
     */
    @Test
    void findById_returns404_whenProductDoesNotExist() throws Exception {
        mockMvc.perform(get("/products/999999").header(HttpHeaders.AUTHORIZATION, bearerToken()))
                .andExpect(status().isNotFound());
    }

    /**
     * L'admin peut créer un produit.
     * The admin can create a product.
     */
    @Test
    void create_returns201_whenAdmin() throws Exception {
        Product product = validProduct();

        mockMvc.perform(post("/products")
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Chaise"));
    }

    /**
     * Un non-admin ne peut pas créer.
     * A non-admin cannot create.
     */
    @Test
    void create_returns403_whenUserIsNotAdmin() throws Exception {
        Product product = validProduct();

        mockMvc.perform(post("/products")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isForbidden());
    }

    /**
     * Rejette un produit sans nom.
     * Rejects a product without a name.
     */
    @Test
    void create_returns400_whenNameIsMissing() throws Exception {
        Product invalid = validProduct();
        invalid.setName(null);

        mockMvc.perform(post("/products")
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    /**
     * L'admin peut modifier un produit.
     * The admin can update a product.
     */
    @Test
    void update_returns200_whenAdmin() throws Exception {
        Product saved = productRepository.save(validProduct());
        Product updated = validProduct();
        updated.setName("Chaise modifiée");

        mockMvc.perform(put("/products/" + saved.getId())
                        .header(HttpHeaders.AUTHORIZATION, adminBearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Chaise modifiée"));
    }

    /**
     * Un non-admin ne peut pas modifier.
     * A non-admin cannot update.
     */
    @Test
    void update_returns403_whenUserIsNotAdmin() throws Exception {
        Product saved = productRepository.save(validProduct());
        Product updated = validProduct();
        updated.setName("Chaise modifiée");

        mockMvc.perform(put("/products/" + saved.getId())
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isForbidden());
    }

    /**
     * L'admin peut supprimer un produit.
     * The admin can delete a product.
     */
    @Test
    void delete_returns204_whenAdmin() throws Exception {
        Product saved = productRepository.save(validProduct());

        mockMvc.perform(delete("/products/" + saved.getId()).header(HttpHeaders.AUTHORIZATION, adminBearerToken()))
                .andExpect(status().isNoContent());
    }

    /**
     * Un non-admin ne peut pas supprimer.
     * A non-admin cannot delete.
     */
    @Test
    void delete_returns403_whenUserIsNotAdmin() throws Exception {
        Product saved = productRepository.save(validProduct());

        mockMvc.perform(delete("/products/" + saved.getId()).header(HttpHeaders.AUTHORIZATION, bearerToken()))
                .andExpect(status().isForbidden());
    }
}
