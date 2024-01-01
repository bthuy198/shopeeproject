package com.thuy.shopeeproject.domain.entity;

import java.util.List;

import org.hibernate.validator.constraints.UniqueElements;

import jakarta.persistence.*;

import com.thuy.shopeeproject.domain.dto.UserCreateResDTO;
import com.thuy.shopeeproject.domain.dto.user.UserResDTO;
import com.thuy.shopeeproject.domain.enums.ERole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "email", nullable = false)
	private String email;
	@Column(name = "password")
	private String password;
	@Column(name = "username", nullable = false)
	private String username;

	@OneToOne
	@JoinColumn(name = "user_info_id")
	private UserInfo userInfo;

	@OneToMany(mappedBy = "user")
	private List<UserAddress> userAddresses;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_role")
	private ERole role;

	@OneToMany(mappedBy = "user")
	private List<Order> orders;

	@OneToOne(mappedBy = "user")
	private Cart cart;

	@OneToOne
	private UserAvatar avatar;

	public UserCreateResDTO toUserCreateResDTO() {
		return new UserCreateResDTO()
				.setId(id)
				.setEmail(email)
				.setUsername(username)
				.setPassword(password)
				.setRole(role.toString())
				.setUserAvatar(avatar.toUserAvatarDTO());
	}

	public UserResDTO toUserResDTO() {
		return new UserResDTO()
				.setId(id)
				.setEmail(email)
				.setUsername(username)
				.setRole(role.toString())
				.setUserAvatar(avatar.toUserAvatarDTO());
	}

	public UserResDTO toUserResDTO(UserInfo userInfo) {
		return new UserResDTO()
				.setId(id)
				.setEmail(email)
				.setUsername(username)
				.setRole(role.toString())
				.setUserInfo(userInfo.toUserInfoResDTO())
				.setUserAvatar(avatar.toUserAvatarDTO());
	}
}
