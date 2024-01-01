package com.thuy.shopeeproject.domain.dto;

import java.math.BigDecimal;

import com.thuy.shopeeproject.domain.dto.product.ProductDetailResDTO;
import com.thuy.shopeeproject.domain.entity.Cart;
import com.thuy.shopeeproject.domain.entity.CartItem;
import com.thuy.shopeeproject.domain.entity.ProductDetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartItemResDTO {
    private Long id;
    private Long quantity;
    private ProductDetailResDTO productDetail;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    public CartItem toCartItem(ProductDetail p, Cart cart) {
        return new CartItem()
                .setProductDetail(p)
                .setQuantity(quantity)
                .setUnitPrice(unitPrice)
                .setTotalPrice(totalPrice)
                .setCart(cart);
    }
}
