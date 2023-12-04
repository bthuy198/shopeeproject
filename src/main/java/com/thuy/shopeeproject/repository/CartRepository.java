package com.thuy.shopeeproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thuy.shopeeproject.domain.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    
}
