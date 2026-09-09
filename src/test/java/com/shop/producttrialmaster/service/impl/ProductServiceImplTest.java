package com.shop.producttrialmaster.service.impl;

import com.shop.producttrialmaster.dto.ProductDto;
import com.shop.producttrialmaster.entity.Product;
import com.shop.producttrialmaster.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    /**
     * Retourne le DTO quand le produit existe.
     * Returns the DTO when the product exists.
     */
    @Test
    void findById_returnsDto_whenProductExists() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Chaise");
        product.setCode("C001");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDto result = productService.findById(1L);

        assertThat(result.getName()).isEqualTo("Chaise");
    }

    /**
     * Lève une exception quand le produit n'existe pas.
     * Throws an exception when the product doesn't exist.
     */
    @Test
    void findById_throws_whenProductDoesNotExist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(99L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Retourne tous les produits sous forme de DTO.
     * Returns all products as DTOs.
     */
    @Test
    void findAll_returnsAllProductsAsDto() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Chaise");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Table");

        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        List<ProductDto> result = productService.findAll();

        assertThat(result).hasSize(2);
    }

    /**
     * Appelle bien deleteById sur le repository.
     * Calls deleteById on the repository.
     */
    @Test
    void delete_callsRepositoryDeleteById() {
        productService.delete(1L);

        verify(productRepository).deleteById(1L);
    }
}
