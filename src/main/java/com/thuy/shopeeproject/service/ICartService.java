package com.thuy.shopeeproject.service;

import com.thuy.shopeeproject.domain.entity.Cart;
import com.thuy.shopeeproject.domain.entity.User;

public interface ICartService extends IService<Cart, Long> {
    Cart createCart(User user);
}
