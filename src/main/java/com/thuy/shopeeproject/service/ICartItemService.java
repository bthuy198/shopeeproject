package com.thuy.shopeeproject.service;

import com.thuy.shopeeproject.domain.entity.CartItem;
import com.thuy.shopeeproject.domain.entity.ProductDetail;

public interface ICartItemService extends IService<CartItem, Long> {
    CartItem findByDetailIdAndCartId(Long productDetailId, Long cartId);
}
