package com.thuy.shopeeproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thuy.shopeeproject.domain.entity.ProductAvatar;

@Repository
public interface ProductAvatarRepository extends JpaRepository<ProductAvatar, String> {
    
}
