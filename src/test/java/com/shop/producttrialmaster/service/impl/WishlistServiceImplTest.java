package com.shop.producttrialmaster.service.impl;

import com.shop.producttrialmaster.entity.Product;
import com.shop.producttrialmaster.entity.User;
import com.shop.producttrialmaster.entity.Wishlist;
import com.shop.producttrialmaster.repository.ProductRepository;
import com.shop.producttrialmaster.repository.UserRepository;
import com.shop.producttrialmaster.repository.WishlistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WishlistServiceImplTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private WishlistServiceImpl wishlistService;

    @Test
    void getWishlist_createsEmptyWishlist_whenNoneExists() {
        User user = User.builder().id(1L).email("user@example.com").build();
        when(wishlistRepository.findByUserEmail("user@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(wishlistRepository.save(any(Wishlist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Wishlist wishlist = wishlistService.getWishlist("user@example.com");

        assertThat(wishlist.getUser().getEmail()).isEqualTo("user@example.com");
        assertThat(wishlist.getProducts()).isEmpty();
    }

    @Test
    void addProduct_addsProductToWishlist() {
        User user = User.builder().id(1L).email("user@example.com").build();
        Wishlist wishlist = Wishlist.builder().id(1L).user(user).build();
        Product product = Product.builder().id(10L).name("Chaise").build();

        when(wishlistRepository.findByUserEmail("user@example.com")).thenReturn(Optional.of(wishlist));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(wishlistRepository.save(any(Wishlist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Wishlist result = wishlistService.addProduct("user@example.com", 10L);

        assertThat(result.getProducts()).hasSize(1);
    }

    @Test
    void removeProduct_removesMatchingProduct() {
        User user = User.builder().id(1L).email("user@example.com").build();
        Product product = Product.builder().id(10L).build();
        Wishlist wishlist = Wishlist.builder().id(1L).user(user).build();
        wishlist.getProducts().add(product);

        when(wishlistRepository.findByUserEmail("user@example.com")).thenReturn(Optional.of(wishlist));
        when(wishlistRepository.save(any(Wishlist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Wishlist result = wishlistService.removeProduct("user@example.com", 10L);

        assertThat(result.getProducts()).isEmpty();
    }
}
