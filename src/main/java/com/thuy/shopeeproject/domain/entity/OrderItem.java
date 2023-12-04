package com.thuy.shopeeproject.domain.entity;

import java.math.BigDecimal;

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
@Table(name="order_item")
public class OrderItem extends BaseEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
    @JoinColumn(name="order_id", referencedColumnName = "id", nullable = false)
	private Order order;
	
	@OneToOne
    @JoinColumn(name = "product_id")
	private Product product;
	private Long quantity;
	private BigDecimal unitPrice;
	private BigDecimal totalPrice;
	
	@ManyToOne
    @JoinColumn(name="cart_id", referencedColumnName = "id", nullable = false)
	private Cart cart;
}
