package com.thuy.shopeeproject.domain.dto;

import java.util.List;

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