package com.thuy.shopeeproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.thuy.shopeeproject.domain.entity.Cart;
import com.thuy.shopeeproject.domain.entity.CartItem;
import com.thuy.shopeeproject.domain.entity.ProductDetail;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query(value = "SELECT * FROM cart_item WHERE product_detail_id = :productDetailId AND cart_id = :cartId", nativeQuery = true)
    CartItem findByDetailIdAndCartId(@Param("productDetailId") Long productDetailId, @Param("cartId") Long cartId);

    // @Query(value = "SELECT * FROM cart_item WHERE product_detail_id =
    // :productDetailId AND cart_id = :cartId", nativeQuery = true)
    // CartItem findByProductIdAndCartId(@Param("productDetailId") Long
    // productDetailId, @Param("cartId") Long cartId);
}
