package com.thuy.shopeeproject.domain.dto;

import java.math.BigDecimal;

import com.thuy.shopeeproject.domain.entity.Order;
import com.thuy.shopeeproject.domain.entity.OrderItem;
import com.thuy.shopeeproject.domain.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItemCreateReqDTO {
    private Long cartItemId;

    // public OrderItem toOrderItem(Order order, Product product) {
    // BigDecimal itemUnitPrice = BigDecimal.valueOf(Long.valueOf(unitPrice));
    // BigDecimal totalPrice =
    // itemUnitPrice.multiply(BigDecimal.valueOf(Long.valueOf(quantity)));
    // return new OrderItem()
    // .setOrder(order)
    // .setProduct(product)
    // .setQuantity(Long.valueOf(quantity))
    // .setUnitPrice(itemUnitPrice)
    // .setTotalPrice(totalPrice);
    // }
}
