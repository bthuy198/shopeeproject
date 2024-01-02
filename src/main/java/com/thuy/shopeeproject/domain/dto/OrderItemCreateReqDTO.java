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
}
