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

    @Test
    void findById_returnsDto_whenProductExists() {
        Product product = Product.builder().id(1L).name("Chaise").code("C001").build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDto result = productService.findById(1L);

        assertThat(result.getName()).isEqualTo("Chaise");
    }

    @Test
    void findById_throws_whenProductDoesNotExist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(99L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findAll_returnsAllProductsAsDto() {
        when(productRepository.findAll()).thenReturn(List.of(
                Product.builder().id(1L).name("Chaise").build(),
                Product.builder().id(2L).name("Table").build()
        ));

        List<ProductDto> result = productService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void delete_callsRepositoryDeleteById() {
        productService.delete(1L);

        verify(productRepository).deleteById(1L);
    }
}
