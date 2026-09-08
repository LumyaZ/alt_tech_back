package com.shop.producttrialmaster.service.impl;

import com.shop.producttrialmaster.entity.Cart;
import com.shop.producttrialmaster.entity.CartItem;
import com.shop.producttrialmaster.entity.Product;
import com.shop.producttrialmaster.entity.User;
import com.shop.producttrialmaster.repository.CartRepository;
import com.shop.producttrialmaster.repository.ProductRepository;
import com.shop.producttrialmaster.repository.UserRepository;
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
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void getCart_createsEmptyCart_whenNoneExists() {
        User user = User.builder().id(1L).email("user@example.com").build();
        when(cartRepository.findByUserEmail("user@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart cart = cartService.getCart("user@example.com");

        assertThat(cart.getUser().getEmail()).isEqualTo("user@example.com");
        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    void addItem_addsNewItem_whenProductNotAlreadyInCart() {
        User user = User.builder().id(1L).email("user@example.com").build();
        Cart cart = Cart.builder().id(1L).user(user).build();
        Product product = Product.builder().id(10L).name("Chaise").build();

        when(cartRepository.findByUserEmail("user@example.com")).thenReturn(Optional.of(cart));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart result = cartService.addItem("user@example.com", 10L, 2);

        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getQuantity()).isEqualTo(2);
    }

    @Test
    void addItem_incrementsQuantity_whenProductAlreadyInCart() {
        User user = User.builder().id(1L).email("user@example.com").build();
        Product product = Product.builder().id(10L).name("Chaise").build();
        Cart cart = Cart.builder().id(1L).user(user).build();
        cart.getItems().add(CartItem.builder().cart(cart).product(product).quantity(1).build());

        when(cartRepository.findByUserEmail("user@example.com")).thenReturn(Optional.of(cart));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart result = cartService.addItem("user@example.com", 10L, 3);

        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getQuantity()).isEqualTo(4);
    }

    @Test
    void removeItem_removesMatchingProduct() {
        User user = User.builder().id(1L).email("user@example.com").build();
        Product product = Product.builder().id(10L).build();
        Cart cart = Cart.builder().id(1L).user(user).build();
        cart.getItems().add(CartItem.builder().cart(cart).product(product).quantity(1).build());

        when(cartRepository.findByUserEmail("user@example.com")).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart result = cartService.removeItem("user@example.com", 10L);

        assertThat(result.getItems()).isEmpty();
    }
}
