package com.thuy.shopeeproject.service;

import org.springframework.data.repository.query.Param;

import com.thuy.shopeeproject.domain.dto.CartItemReqDTO;
import com.thuy.shopeeproject.domain.dto.CartItemUpdateReqDTO;
import com.thuy.shopeeproject.domain.entity.Cart;
import com.thuy.shopeeproject.domain.entity.CartItem;
import com.thuy.shopeeproject.domain.entity.ProductDetail;

public interface ICartItemService extends IService<CartItem, Long> {
    CartItem findByDetailIdAndCartId(Long productDetailId, Long cartId);

    CartItem addToCart(Cart cart, ProductDetail productDetail, CartItemReqDTO cartItemReqDTO);

    CartItem updateCart(CartItem cartItem, Cart cart, CartItemUpdateReqDTO cartItemUpdateReqDTO);

    CartItem findByDetailId(Long productDetailId);
}
