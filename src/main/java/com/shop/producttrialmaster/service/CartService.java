package com.shop.producttrialmaster.service;

import com.shop.producttrialmaster.entity.Cart;

public interface CartService {

    /**
     * Récupère le panier de l'utilisateur, en crée un vide si besoin.
     * Retrieves the user's cart, creating an empty one if needed.
     */
    Cart getCart(String email);

    /**
     * Ajoute un produit au panier (ou augmente sa quantité s'il y est déjà).
     * Adds a product to the cart (or increases its quantity if already present).
     */
    Cart addItem(String email, Long productId, Integer quantity);

    /**
     * Retire un produit du panier.
     * Removes a product from the cart.
     */
    Cart removeItem(String email, Long productId);
}
