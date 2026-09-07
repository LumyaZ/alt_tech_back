package com.shop.producttrialmaster.service;

import java.util.List;

import com.shop.producttrialmaster.dto.ProductDto;
import com.shop.producttrialmaster.entity.Product;

public interface ProductService {

    /**
     * Récupère tous les produits.
     * Retrieves all products.
     */
    List<ProductDto> findAll();

    /**
     * Récupère un produit par son id, échoue si aucun ne correspond.
     * Retrieves a product by its id, throws if none matches.
     */
    ProductDto findById(Long id);

    /**
     * Crée un nouveau produit.
     * Creates a new product.
     */
    ProductDto create(Product product);

    /**
     * Met à jour un produit existant identifié par son id.
     * Updates an existing product identified by its id.
     */
    ProductDto update(Long id, Product product);

    /**
     * Supprime un produit par son id.
     * Deletes a product by its id.
     */
    void delete(Long id);
}
