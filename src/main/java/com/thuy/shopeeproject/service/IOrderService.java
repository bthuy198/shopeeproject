package com.thuy.shopeeproject.service;

import java.util.List;

import com.thuy.shopeeproject.domain.dto.OrderCreateReqDTO;
import com.thuy.shopeeproject.domain.entity.Cart;
import com.thuy.shopeeproject.domain.entity.Order;
import com.thuy.shopeeproject.domain.enums.EOrderStatus;

public interface IOrderService extends IService<Order, Long> {
    Order createOrder(Order order, Cart cart, OrderCreateReqDTO orderCreateReqDTO);

    List<Order> findByOrderStatus(EOrderStatus orderStatus);

    Order cancelOrder(Order order);
}
