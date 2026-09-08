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

    @GetMapping
    public Cart getCart(Authentication authentication) {
        return cartService.getCart(authentication.getName());
    }

    @PostMapping("/items")
    public Cart addItem(Authentication authentication, @Valid @RequestBody CartItemRequest request) {
        return cartService.addItem(authentication.getName(), request.getProductId(), request.getQuantity());
    }

    @DeleteMapping("/items/{productId}")
    public Cart removeItem(Authentication authentication, @PathVariable Long productId) {
        return cartService.removeItem(authentication.getName(), productId);
    }

    @PutMapping("/items/{productId}")
    public Cart updateItemQuantity(Authentication authentication, @PathVariable Long productId, @Valid @RequestBody UpdateCartItemQuantityRequest request) {
        return cartService.updateItemQuantity(authentication.getName(), productId, request.getQuantity());
    }
}
