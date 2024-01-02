package com.thuy.shopeeproject.domain.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.thuy.shopeeproject.domain.dto.BillResDTO;
import com.thuy.shopeeproject.domain.dto.OrderItemResDTO;
import com.thuy.shopeeproject.domain.enums.EPaymentMethod;

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
@Table(name = "bills")
public class Bill extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "order_id", referencedColumnName = "id")
	private Order order;
	private BigDecimal total;
	private Date date;

	public BillResDTO toBillResDTO() {
		List<OrderItemResDTO> orderItemResDTOs = new ArrayList<>();
		for (OrderItem item : order.getOrderItem()) {
			OrderItemResDTO orderItemResDTO = item.toOrderItemResDTO();
			orderItemResDTOs.add(orderItemResDTO);
		}
		return new BillResDTO()
				.setId(id)
				.setAddress(order.getAddress())
				.setDate(date.toString())
				.setName(order.getName())
				.setOrderItems(orderItemResDTOs)
				.setPaymentMethod(order.getPaymentMethod().getPaymentMethod().getValue())
				.setPhone(order.getPhone())
				.setTotal(total);
	}
}
