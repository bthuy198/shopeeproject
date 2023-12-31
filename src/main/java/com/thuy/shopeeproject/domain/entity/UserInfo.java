package com.thuy.shopeeproject.domain.entity;

import java.util.Date;

import jakarta.persistence.*;

import com.thuy.shopeeproject.domain.dto.user.UserInfoResDTO;
import com.thuy.shopeeproject.domain.enums.EGender;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_infos")
public class UserInfo extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(mappedBy = "userInfo")
	private User user;

	@Column(name = "name")
	private String name;

	@Column(name = "phone", unique = true)
	private String phone;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender")
	private EGender gender;

	@Column(name = "birthday")
	private Date birthday;

	public UserInfoResDTO toUserInfoResDTO() {
		return new UserInfoResDTO()
				.setId(id)
				.setName(name)
				.setGender(gender.getValue())
				.setBirthday(birthday)
				.setPhone(phone);
	}
}
