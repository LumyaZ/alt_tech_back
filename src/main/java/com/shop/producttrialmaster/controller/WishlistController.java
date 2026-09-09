package com.shop.producttrialmaster.controller;

import com.shop.producttrialmaster.entity.Wishlist;
import com.shop.producttrialmaster.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    /**
     * Récupère la liste d'envie de l'utilisateur connecté.
     * Retrieves the authenticated user's wishlist.
     */
    @GetMapping
    public Wishlist getWishlist(Authentication authentication) {
        return wishlistService.getWishlist(authentication.getName());
    }

    /**
     * Ajoute un produit à la liste d'envie.
     * Adds a product to the wishlist.
     */
    @PostMapping("/products/{productId}")
    public Wishlist addProduct(Authentication authentication, @PathVariable Long productId) {
        return wishlistService.addProduct(authentication.getName(), productId);
    }

    /**
     * Retire un produit de la liste d'envie.
     * Removes a product from the wishlist.
     */
    @DeleteMapping("/products/{productId}")
    public Wishlist removeProduct(Authentication authentication, @PathVariable Long productId) {
        return wishlistService.removeProduct(authentication.getName(), productId);
    }
}
