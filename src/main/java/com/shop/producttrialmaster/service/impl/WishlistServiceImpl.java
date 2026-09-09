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

import java.util.Iterator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public Wishlist getWishlist(String email) {
        Optional<Wishlist> wishlistOptional = wishlistRepository.findByUserEmail(email);
        if (wishlistOptional.isEmpty()) {
            return createWishlistForUser(email);
        }
        return wishlistOptional.get();
    }

    @Override
    public Wishlist addProduct(String email, Long productId) {
        Wishlist wishlist = getWishlist(email);

        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new IllegalArgumentException("Produit introuvable : " + productId);
        }

        wishlist.getProducts().add(productOptional.get());

        return wishlistRepository.save(wishlist);
    }

    @Override
    public Wishlist removeProduct(String email, Long productId) {
        Wishlist wishlist = getWishlist(email);

        Iterator<Product> iterator = wishlist.getProducts().iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getId().equals(productId)) {
                iterator.remove();
            }
        }

        return wishlistRepository.save(wishlist);
    }

    private Wishlist createWishlistForUser(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Utilisateur introuvable : " + email);
        }
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(userOptional.get());
        return wishlistRepository.save(wishlist);
    }
}
