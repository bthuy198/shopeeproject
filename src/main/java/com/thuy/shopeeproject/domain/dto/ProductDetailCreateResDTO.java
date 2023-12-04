package com.thuy.shopeeproject.domain.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDetailCreateResDTO {
    private Long id;
	private SizeDTO size;
	private BigDecimal price;
	private Long quantity;
}
