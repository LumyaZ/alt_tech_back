package com.shop.producttrialmaster.controller;

import com.shop.producttrialmaster.dto.CartItemRequest;
import com.shop.producttrialmaster.dto.UpdateCartItemQuantityRequest;
import com.shop.producttrialmaster.entity.Cart;
import com.shop.producttrialmaster.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * Récupère le panier de l'utilisateur connecté.
     * Retrieves the authenticated user's cart.
     */
    @GetMapping
    public Cart getCart(Authentication authentication) {
        return cartService.getCart(authentication.getName());
    }

    /**
     * Ajoute un produit au panier (ou augmente sa quantité s'il y est déjà).
     * Adds a product to the cart (or increases its quantity if already present).
     */
    @PostMapping("/items")
    public Cart addItem(Authentication authentication, @Valid @RequestBody CartItemRequest request) {
        return cartService.addItem(authentication.getName(), request.getProductId(), request.getQuantity());
    }

    /**
     * Retire un produit du panier.
     * Removes a product from the cart.
     */
    @DeleteMapping("/items/{productId}")
    public Cart removeItem(Authentication authentication, @PathVariable Long productId) {
        return cartService.removeItem(authentication.getName(), productId);
    }

    /**
     * Fixe la quantité d'un produit dans le panier (le retire si elle tombe à 0).
     * Sets a product's quantity in the cart (removes it if it reaches 0).
     */
    @PutMapping("/items/{productId}")
    public Cart updateItemQuantity(Authentication authentication, @PathVariable Long productId, @Valid @RequestBody UpdateCartItemQuantityRequest request) {
        return cartService.updateItemQuantity(authentication.getName(), productId, request.getQuantity());
    }
}
