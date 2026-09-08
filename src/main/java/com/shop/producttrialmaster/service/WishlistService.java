package com.shop.producttrialmaster.service;

import com.shop.producttrialmaster.entity.Wishlist;

public interface WishlistService {

    /**
     * Récupère la liste d'envie de l'utilisateur, en crée une vide si besoin.
     * Retrieves the user's wishlist, creating an empty one if needed.
     */
    Wishlist getWishlist(String email);

    /**
     * Ajoute un produit à la liste d'envie.
     * Adds a product to the wishlist.
     */
    Wishlist addProduct(String email, Long productId);

    /**
     * Retire un produit de la liste d'envie.
     * Removes a product from the wishlist.
     */
    Wishlist removeProduct(String email, Long productId);
}
