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
public class ProductResDTO {
	private Long id;

	private String productName;

	private List<ProductAvatarDTO> productAvatars;

	private CategoryDTO category;

	private List<ProductDetailResDTO> productDetails;

	private String description;

}
