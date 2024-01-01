package com.thuy.shopeeproject.domain.dto.product;

import java.util.List;

import com.thuy.shopeeproject.domain.dto.CategoryDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductCreateResDTO {
	private Long id;

	private String productName;

	// private List<Avatar> productAvatars;

	private CategoryDTO category;

	private List<ProductDetailCreateResDTO> productDetails;

	private String description;
}
