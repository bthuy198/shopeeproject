package com.thuy.shopeeproject.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

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
@Table(name="bills")
public class Bill extends BaseEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
	private Order order;
	private BigDecimal total;
	private Date date;
}
