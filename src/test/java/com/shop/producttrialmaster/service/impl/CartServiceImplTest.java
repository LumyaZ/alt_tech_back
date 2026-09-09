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

    /**
     * Crée un panier vide si l'utilisateur n'en a pas.
     * Creates an empty cart if the user has none.
     */
    @Test
    void getCart_createsEmptyCart_whenNoneExists() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        when(cartRepository.findByUserEmail("user@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart cart = cartService.getCart("user@example.com");

        assertThat(cart.getUser().getEmail()).isEqualTo("user@example.com");
        assertThat(cart.getItems()).isEmpty();
    }

    /**
     * Ajoute un nouvel article si le produit n'est pas déjà dans le panier.
     * Adds a new item if the product isn't already in the cart.
     */
    @Test
    void addItem_addsNewItem_whenProductNotAlreadyInCart() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);

        Product product = new Product();
        product.setId(10L);
        product.setName("Chaise");

        when(cartRepository.findByUserEmail("user@example.com")).thenReturn(Optional.of(cart));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart result = cartService.addItem("user@example.com", 10L, 2);

        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getQuantity()).isEqualTo(2);
    }

    /**
     * Incrémente la quantité si le produit est déjà dans le panier.
     * Increments the quantity if the product is already in the cart.
     */
    @Test
    void addItem_incrementsQuantity_whenProductAlreadyInCart() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        Product product = new Product();
        product.setId(10L);
        product.setName("Chaise");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);

        CartItem existingItem = new CartItem();
        existingItem.setCart(cart);
        existingItem.setProduct(product);
        existingItem.setQuantity(1);
        cart.getItems().add(existingItem);

        when(cartRepository.findByUserEmail("user@example.com")).thenReturn(Optional.of(cart));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart result = cartService.addItem("user@example.com", 10L, 3);

        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getQuantity()).isEqualTo(4);
    }

    /**
     * Retire le produit correspondant du panier.
     * Removes the matching product from the cart.
     */
    @Test
    void removeItem_removesMatchingProduct() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        Product product = new Product();
        product.setId(10L);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(1);
        cart.getItems().add(item);

        when(cartRepository.findByUserEmail("user@example.com")).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart result = cartService.removeItem("user@example.com", 10L);

        assertThat(result.getItems()).isEmpty();
    }
}
