package com.thuy.shopeeproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.thuy.shopeeproject.domain.entity.ProductDetail;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    @Query(value = "SELECT * FROM product_detail WHERE product_detail.product_id = :productId AND product_detail.size_id = :sizeId", nativeQuery = true)
    ProductDetail findByProductIdAndSize(@Param("productId") Long productId, @Param("sizeId") Long sizeId);
}
