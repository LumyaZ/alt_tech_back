package com.shop.producttrialmaster.repository;

import com.shop.producttrialmaster.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Trouve le panier associé à l'email de l'utilisateur (traverse la relation Cart -> User).
     * Finds the cart associated with the user's email (traverses the Cart -> User relation).
     */
    Optional<Cart> findByUserEmail(String email);
}
