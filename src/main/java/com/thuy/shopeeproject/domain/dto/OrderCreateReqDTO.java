package com.thuy.shopeeproject.domain.dto;

import java.math.BigDecimal;
import java.util.List;

import com.thuy.shopeeproject.domain.dto.user.UserAddressDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderCreateReqDTO {
    private String userAddressId;
    private String paymentMethodId;
    private List<OrderItemCreateReqDTO> orderItems;
}
