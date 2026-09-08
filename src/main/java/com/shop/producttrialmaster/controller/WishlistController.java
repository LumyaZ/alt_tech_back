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

    @GetMapping
    public Wishlist getWishlist(Authentication authentication) {
        return wishlistService.getWishlist(authentication.getName());
    }

    @PostMapping("/products/{productId}")
    public Wishlist addProduct(Authentication authentication, @PathVariable Long productId) {
        return wishlistService.addProduct(authentication.getName(), productId);
    }

    @DeleteMapping("/products/{productId}")
    public Wishlist removeProduct(Authentication authentication, @PathVariable Long productId) {
        return wishlistService.removeProduct(authentication.getName(), productId);
    }
}
