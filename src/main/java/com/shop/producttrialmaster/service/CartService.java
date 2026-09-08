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

    /**
     * Fixe la quantité d'un produit dans le panier (le retire si la quantité tombe à 0).
     * Sets a product's quantity in the cart (removes it if quantity reaches 0).
     */
    Cart updateItemQuantity(String email, Long productId, Integer quantity);
}
