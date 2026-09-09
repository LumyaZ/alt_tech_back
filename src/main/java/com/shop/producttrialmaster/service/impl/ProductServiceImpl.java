package com.shop.producttrialmaster.service.impl;

import com.shop.producttrialmaster.dto.ProductDto;
import com.shop.producttrialmaster.entity.Product;
import com.shop.producttrialmaster.repository.ProductRepository;
import com.shop.producttrialmaster.service.ProductService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductDto> findAll() {
        List<Product> products = productRepository.findAll();

        List<ProductDto> productsDtos = new ArrayList<>();
        for (Product product : products) {
            productsDtos.add(convertToDto(product));
        }

        return productsDtos;
    }

    @Override
    public ProductDto findById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new IllegalArgumentException("Produit introuvable : " + id);
        }
        Product product = productOptional.get();
        return convertToDto(product);
    }

    @Override
    public ProductDto create(Product product) {
        return convertToDto(productRepository.save(product));
    }

    @Override
    public ProductDto update(Long id, Product product) {
        Optional<Product> existingOptional = productRepository.findById(id);
        if (existingOptional.isEmpty()) {
            throw new IllegalArgumentException("Produit introuvable : " + id);
        }
        Product existing = existingOptional.get();

        existing.setCode(product.getCode());
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setImage(product.getImage());
        existing.setCategory(product.getCategory());
        existing.setPrice(product.getPrice());
        existing.setQuantity(product.getQuantity());
        existing.setInternalReference(product.getInternalReference());
        existing.setShellId(product.getShellId());
        existing.setInventoryStatus(product.getInventoryStatus());
        existing.setRating(product.getRating());
        return convertToDto(productRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setCode(product.getCode());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setImage(product.getImage());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setInternalReference(product.getInternalReference());
        dto.setShellId(product.getShellId());
        dto.setInventoryStatus(product.getInventoryStatus());
        dto.setRating(product.getRating());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }
}
