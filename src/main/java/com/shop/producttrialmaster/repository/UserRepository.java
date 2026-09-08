package com.shop.producttrialmaster.repository;

import com.shop.producttrialmaster.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email.
     * Trouver un utilisateur par email.
     */
    Optional<User> findByEmail(String email);
}