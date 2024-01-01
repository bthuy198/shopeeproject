package com.thuy.shopeeproject.domain.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thuy.shopeeproject.domain.dto.product.ProductAvatarDTO;
import com.thuy.shopeeproject.domain.dto.product.ProductCreateResDTO;
import com.thuy.shopeeproject.domain.dto.product.ProductDetailCreateResDTO;
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
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "product_name")
	private String productName;

	@OneToMany(mappedBy = "product", targetEntity = ProductAvatar.class, fetch = FetchType.LAZY)
	@JsonIgnoreProperties("productAvatars")
	private List<ProductAvatar> productAvatars;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@OneToMany(mappedBy = "product", targetEntity = ProductDetail.class, fetch = FetchType.LAZY)
	private List<ProductDetail> productDetails;

	@Column(name = "description")
	private String description;

	public ProductCreateResDTO toProductCreateResDTO() {
		List<ProductDetailCreateResDTO> productDetailCreateResDTOS = new ArrayList<ProductDetailCreateResDTO>();
		for (ProductDetail item : productDetails) {
			ProductDetailCreateResDTO productDetailCreateResDTO = item.toProductDetailCreateResDTO();
			productDetailCreateResDTOS.add(productDetailCreateResDTO);
		}
		return new ProductCreateResDTO()
				.setId(id)
				.setProductName(productName)
				.setCategory(category.toCategoryDTO())
				.setProductDetails(productDetailCreateResDTOS)
				.setDescription(description);
	}

	public ProductCreateResDTO toProductCreateResDTO(List<ProductAvatar> avatars) {
		List<ProductDetailCreateResDTO> productDetailCreateResDTOS = new ArrayList<ProductDetailCreateResDTO>();
		for (ProductDetail item : productDetails) {
			ProductDetailCreateResDTO productDetailCreateResDTO = item.toProductDetailCreateResDTO();
			productDetailCreateResDTOS.add(productDetailCreateResDTO);
		}
		return new ProductCreateResDTO()
				.setId(id)
				.setProductName(productName)
				.setCategory(category.toCategoryDTO())
				.setProductDetails(productDetailCreateResDTOS)
				.setDescription(description);
	}

	public ProductResDTO toProductResDTO() {
		List<ProductAvatarDTO> productAvatarDTOS = new ArrayList<>();
		for (ProductAvatar item : productAvatars) {
			ProductAvatarDTO productAvatarDTO = item.toProductAvatarDTO();
			productAvatarDTOS.add(productAvatarDTO);
		}
		List<ProductDetailResDTO> productDetailResDTOs = new ArrayList<>();
		for (ProductDetail item : productDetails) {
			ProductDetailResDTO productDetailResDTO = item.toProductDetailResDTO();
			productDetailResDTOs.add(productDetailResDTO);
		}
		return new ProductResDTO()
				.setId(id)
				.setCategory(category.toCategoryDTO())
				.setDescription(description)
				.setProductName(productName)
				.setProductAvatars(productAvatarDTOS)
				.setProductDetails(productDetailResDTOs);
	}
}
