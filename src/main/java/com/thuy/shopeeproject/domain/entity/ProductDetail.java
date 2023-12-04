package com.thuy.shopeeproject.domain.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;

import com.thuy.shopeeproject.domain.dto.ProductDetailCreateResDTO;
import com.thuy.shopeeproject.domain.dto.ProductDetailResDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product_detail")
public class ProductDetail extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
	private Product product;

	@ManyToOne
	@JoinColumn(name = "size_id", referencedColumnName = "id", nullable = false)
	private Size size;
	private BigDecimal price;
	private Long quantity;

	@Column(name = "sold_out")
	private Boolean sold_out = false;

	// public ProductDetail setQuantity(Long quantity) {
	// this.quantity = quantity;
	// this.sold_out = (this.quantity == 0);
	// }

	// public void setSoldOut() {
	// this.sold_out = (this.quantity == 0);
	// }

	public ProductDetailCreateResDTO toProductDetailCreateResDTO() {
		return new ProductDetailCreateResDTO()
				.setId(id)
				.setPrice(price)
				.setQuantity(quantity)
				.setSize(size.toSizeDTO());
	}

	public ProductDetailResDTO toProductDetailResDTO() {
		return new ProductDetailResDTO()
				.setId(id)
				.setProductName(product.getProductName())
				.setPrice(price)
				.setQuantity(quantity)
				.setSize(size.toSizeDTO())
				.setCategory(product.getCategory().toCategoryDTO());
	}

}
