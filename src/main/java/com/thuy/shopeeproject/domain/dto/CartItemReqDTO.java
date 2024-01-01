package com.thuy.shopeeproject.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartItemReqDTO {
    private Long quantity;
    private Long sizeId;
}
