package com.shop.producttrialmaster.service.impl;

import com.shop.producttrialmaster.entity.Product;
import com.shop.producttrialmaster.entity.User;
import com.shop.producttrialmaster.entity.Wishlist;
import com.shop.producttrialmaster.repository.ProductRepository;
import com.shop.producttrialmaster.repository.UserRepository;
import com.shop.producttrialmaster.repository.WishlistRepository;
import com.shop.producttrialmaster.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public Wishlist getWishlist(String email) {
        return wishlistRepository.findByUserEmail(email)
                .orElseGet(() -> createWishlistForUser(email));
    }

    @Override
    public Wishlist addProduct(String email, Long productId) {
        Wishlist wishlist = getWishlist(email);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Produit introuvable : " + productId));

        wishlist.getProducts().add(product);

        return wishlistRepository.save(wishlist);
    }

    @Override
    public Wishlist removeProduct(String email, Long productId) {
        Wishlist wishlist = getWishlist(email);
        wishlist.getProducts().removeIf(product -> product.getId().equals(productId));
        return wishlistRepository.save(wishlist);
    }

    private Wishlist createWishlistForUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable : " + email));
        Wishlist wishlist = Wishlist.builder().user(user).build();
        return wishlistRepository.save(wishlist);
    }
}
