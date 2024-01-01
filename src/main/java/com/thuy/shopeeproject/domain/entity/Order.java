package com.thuy.shopeeproject.domain.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import com.thuy.shopeeproject.domain.dto.OrderItemResDTO;
import com.thuy.shopeeproject.domain.dto.OrderResDTO;
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
@Table(name = "orders")
public class Order extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "users_id", referencedColumnName = "id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "payment_method_id", referencedColumnName = "id", nullable = false)
	private PaymentMethod paymentMethod;

	@Column(name = "total")
	private BigDecimal total;

	@OneToMany(mappedBy = "order")
	private List<OrderItem> orderItem;

	@Enumerated(EnumType.STRING)
	@Column(name = "order_status")
	private EOrderStatus orderStatus;

	@Column(name = "name")
	private String name;

	@Column(name = "phone")
	private String phone;

	@Column(name = "address")
	private String address;

	public OrderResDTO toOrderResDTO() {
		List<OrderItemResDTO> orderItemResDTOs = new ArrayList<>();
		for (OrderItem item : orderItem) {
			OrderItemResDTO orderItemResDTO = item.toOrderItemResDTO();
			orderItemResDTOs.add(orderItemResDTO);
		}
		return new OrderResDTO()
				.setId(id)
				.setAddress(address)
				.setName(name)
				.setOrderItems(orderItemResDTOs)
				.setPhone(phone)
				.setPaymentMethod(paymentMethod.getPaymentMethod().getValue())
				.setOrderStatus(orderStatus.getValue())
				.setTotal(total);
	}

}
