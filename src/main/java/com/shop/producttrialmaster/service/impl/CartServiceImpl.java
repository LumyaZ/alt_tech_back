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

import java.util.Iterator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public Cart getCart(String email) {
        Optional<Cart> cartOptional = cartRepository.findByUserEmail(email);
        if (cartOptional.isEmpty()) {
            return createCartForUser(email);
        }
        return cartOptional.get();
    }

    @Override
    public Cart addItem(String email, Long productId, Integer quantity) {
        Cart cart = getCart(email);

        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new IllegalArgumentException("Produit introuvable : " + productId);
        }
        Product product = productOptional.get();

        CartItem existingItem = findItemByProductId(cart, productId);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }

        return cartRepository.save(cart);
    }

    @Override
    public Cart removeItem(String email, Long productId) {
        Cart cart = getCart(email);
        removeItemByProductId(cart, productId);
        return cartRepository.save(cart);
    }

    @Override
    public Cart updateItemQuantity(String email, Long productId, Integer quantity) {
        Cart cart = getCart(email);

        if (quantity == 0) {
            removeItemByProductId(cart, productId);
        } else {
            CartItem existingItem = findItemByProductId(cart, productId);
            if (existingItem != null) {
                existingItem.setQuantity(quantity);
            }
        }

        return cartRepository.save(cart);
    }

    private Cart createCartForUser(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Utilisateur introuvable : " + email);
        }
        Cart cart = new Cart();
        cart.setUser(userOptional.get());
        return cartRepository.save(cart);
    }

    /**
     * Cherche la ligne de panier correspondant au produit, null si absente.
     * Finds the cart line matching the product, null if absent.
     */
    private CartItem findItemByProductId(Cart cart, Long productId) {
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Retire la ligne de panier correspondant au produit, si elle existe.
     * Removes the cart line matching the product, if present.
     */
    private void removeItemByProductId(Cart cart, Long productId) {
        Iterator<CartItem> iterator = cart.getItems().iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getProduct().getId().equals(productId)) {
                iterator.remove();
            }
        }
    }
}
