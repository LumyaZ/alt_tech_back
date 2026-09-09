package com.shop.producttrialmaster.repository;

import com.shop.producttrialmaster.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    /**
     * Trouve la liste d'envie associée à l'email de l'utilisateur (traverse la relation Wishlist -> User).
     * Finds the wishlist associated with the user's email (traverses the Wishlist -> User relation).
     */
    Optional<Wishlist> findByUserEmail(String email);
}
