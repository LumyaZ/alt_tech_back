package com.shop.producttrialmaster.service.impl;

import com.shop.producttrialmaster.entity.Cart;
import com.shop.producttrialmaster.entity.CartItem;
import com.shop.producttrialmaster.entity.Product;
import com.shop.producttrialmaster.entity.User;
import com.shop.producttrialmaster.repository.CartRepository;
import com.shop.producttrialmaster.repository.ProductRepository;
import com.shop.producttrialmaster.repository.UserRepository;
import com.shop.producttrialmaster.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public Cart getCart(String email) {
        return cartRepository.findByUserEmail(email)
                .orElseGet(() -> createCartForUser(email));
    }

    @Override
    public Cart addItem(String email, Long productId, Integer quantity) {
        Cart cart = getCart(email);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Produit introuvable : " + productId));

        cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + quantity),
                        () -> cart.getItems().add(CartItem.builder()
                                .cart(cart)
                                .product(product)
                                .quantity(quantity)
                                .build())
                );

        return cartRepository.save(cart);
    }

    @Override
    public Cart removeItem(String email, Long productId) {
        Cart cart = getCart(email);
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        return cartRepository.save(cart);
    }

    private Cart createCartForUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable : " + email));
        Cart cart = Cart.builder().user(user).build();
        return cartRepository.save(cart);
    }
}
