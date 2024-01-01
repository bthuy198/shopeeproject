package com.thuy.shopeeproject.service;

import com.thuy.shopeeproject.domain.dto.OrderCreateReqDTO;
import com.thuy.shopeeproject.domain.entity.Cart;
import com.thuy.shopeeproject.domain.entity.Order;

public interface IOrderService extends IService<Order, Long> {
    Order createOrder(Order order, Cart cart, OrderCreateReqDTO orderCreateReqDTO);
}
