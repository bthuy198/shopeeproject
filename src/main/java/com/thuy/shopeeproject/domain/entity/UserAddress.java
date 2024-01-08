package com.thuy.shopeeproject.domain.entity;

import org.hibernate.annotations.Where;

import com.thuy.shopeeproject.domain.dto.user.UserAddressDTO;

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
@Table(name = "user_address")
@Where(clause = "deleted = false")
public class UserAddress extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "name")
	private String name;
	@Column(name = "phone")
	private String phone;
	@Column(name = "address")
	private String address;
	@Column(name = "is_default")
	private Boolean isDefault = false;

	@ManyToOne
	@JoinColumn(name = "users_id", referencedColumnName = "id", nullable = false)
	private User user;

	public UserAddressDTO toUserAddressDTO() {
		return new UserAddressDTO()
				.setAddress(address)
				.setName(name)
				.setPhone(phone);
	}

}