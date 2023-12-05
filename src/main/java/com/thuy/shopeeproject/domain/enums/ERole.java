package com.thuy.shopeeproject.domain.enums;

public enum ERole {
	ADMIN("admin"),
	USER("user");

	private String value;

	ERole(String value) {
		// TODO Auto-generated constructor stub
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public static ERole getByValue(String value) {
		for (ERole role : values()) {
			if (role.getValue().equalsIgnoreCase(value)) {
				return role;
			}
		}
		throw new IllegalArgumentException("Không tìm thấy giá trị enum cho " + value);
	}

}
