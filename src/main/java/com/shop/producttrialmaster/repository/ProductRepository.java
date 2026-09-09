package com.shop.producttrialmaster.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop.producttrialmaster.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
