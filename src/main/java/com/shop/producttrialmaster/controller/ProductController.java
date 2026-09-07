package com.shop.producttrialmaster.controller;

import com.shop.producttrialmaster.dto.ProductDto;
import com.shop.producttrialmaster.entity.Product;
import com.shop.producttrialmaster.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Liste tous les produits.
     * Lists all products.
     */
    @GetMapping
    public List<ProductDto> findAll() {
        return productService.findAll();
    }

    /**
     * Récupère un produit par son id.
     * Retrieves a single product by its id.
     */
    @GetMapping("/{id}")
    public ProductDto findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    /**
     * Crée un nouveau produit.
     * Creates a new product.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto create(@Valid @RequestBody Product product) {
        return productService.create(product);
    }

    /**
     * Met à jour un produit existant.
     * Updates an existing product.
     */
    @PutMapping("/{id}")
    public ProductDto update(@PathVariable Long id, @Valid @RequestBody Product product) {
        return productService.update(id, product);
    }

    /**
     * Supprime un produit.
     * Deletes a product.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}