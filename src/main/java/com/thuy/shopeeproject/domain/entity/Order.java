package com.thuy.shopeeproject.domain.entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.*;

import com.thuy.shopeeproject.domain.enums.EOrderStatus;
import com.thuy.shopeeproject.domain.enums.EPaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="orders")
public class Order extends BaseEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
    @JoinColumn(name="users_id", referencedColumnName = "id", nullable = false)
	private User user;
	private EPaymentMethod paymentMethod;
	private BigDecimal totalPayment;
	
	@OneToMany(mappedBy = "order")
	private List<OrderItem> orderItem;
	
	@OneToOne
    @JoinColumn(name = "address_id")
	private UserAddress address;
	private EOrderStatus orderStatus;
	
}
