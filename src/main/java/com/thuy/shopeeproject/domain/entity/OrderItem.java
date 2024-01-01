package com.thuy.shopeeproject.domain.entity;

import java.math.BigDecimal;

import com.thuy.shopeeproject.domain.dto.OrderItemResDTO;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "order_item")
public class OrderItem extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
	private Order order;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@Column(name = "quantity")
	private Long quantity;

	@Column(name = "unit_price")
	private BigDecimal unitPrice;

	@Column(name = "total_price")
	private BigDecimal totalPrice;

	@Column(name = "size")
	private String size;

	public OrderItemResDTO toOrderItemResDTO() {
		return new OrderItemResDTO()
				.setId(id)
				.setProduct(product.toProductResDTO())
				.setQuantity(quantity)
				.setSize(size)
				.setUnitPrice(unitPrice)
				.setTotalPrice(totalPrice);
	}

}
