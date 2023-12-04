package com.thuy.shopeeproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thuy.shopeeproject.domain.entity.Size;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {
    
}
