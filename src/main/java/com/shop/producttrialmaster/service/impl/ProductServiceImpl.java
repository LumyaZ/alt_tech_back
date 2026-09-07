package com.shop.producttrialmaster.service.impl;

import com.shop.producttrialmaster.dto.ProductDto;
import com.shop.producttrialmaster.entity.Product;
import com.shop.producttrialmaster.repository.ProductRepository;
import com.shop.producttrialmaster.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductDto> findAll() {
        return productRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public ProductDto findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produit introuvable : " + id));
        return convertToDto(product);
    }

    @Override
    public ProductDto create(Product product) {
        return convertToDto(productRepository.save(product));
    }

    @Override
    public ProductDto update(Long id, Product product) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produit introuvable : " + id));
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

    // Reconvertie à chaque lecture : la seule fonction de mapping conservée (entité -> dto)
    private ProductDto convertToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .image(product.getImage())
                .category(product.getCategory())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .internalReference(product.getInternalReference())
                .shellId(product.getShellId())
                .inventoryStatus(product.getInventoryStatus())
                .rating(product.getRating())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
