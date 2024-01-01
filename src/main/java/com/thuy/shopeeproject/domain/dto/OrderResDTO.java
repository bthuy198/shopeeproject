package com.thuy.shopeeproject.domain.dto;

import java.math.BigDecimal;
import java.util.List;

import com.thuy.shopeeproject.domain.entity.OrderItem;
import com.thuy.shopeeproject.domain.entity.PaymentMethod;
import com.thuy.shopeeproject.domain.enums.EOrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderResDTO {
    private Long id;
    private String paymentMethod;
    private BigDecimal total;
    private List<OrderItemResDTO> orderItems;
    private String orderStatus;
    private String name;
    private String phone;
    private String address;
}
