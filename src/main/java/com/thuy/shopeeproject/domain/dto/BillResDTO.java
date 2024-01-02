package com.thuy.shopeeproject.domain.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BillResDTO {
    private Long id;
    private String paymentMethod;
    private BigDecimal total;
    private List<OrderItemResDTO> orderItems;
    private String name;
    private String phone;
    private String address;
    private String date;
}
