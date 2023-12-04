package com.thuy.shopeeproject.domain.entity;

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
@Table(name="user_address")
public class UserAddress extends BaseEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name="name")
	private String name;
	@Column(name="phone")
	private String phone;
	@Column(name="address")
	private String address;
	@Column(name="is_default")
	private Boolean isDefault = false;
	
	@ManyToOne
    @JoinColumn(name="users_id", referencedColumnName = "id", nullable = false)
	private User user;
	
	@OneToOne(mappedBy = "address")
	private Order order;
}