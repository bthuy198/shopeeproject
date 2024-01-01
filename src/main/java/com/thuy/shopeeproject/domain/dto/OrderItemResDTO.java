package com.thuy.shopeeproject.domain.dto;

import java.math.BigDecimal;

import com.thuy.shopeeproject.domain.dto.product.ProductDetailResDTO;
import com.thuy.shopeeproject.domain.dto.product.ProductResDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItemResDTO {
    private Long id;
    private Long quantity;
    private ProductResDTO product;
    private String size;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
