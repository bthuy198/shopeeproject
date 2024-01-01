package com.thuy.shopeeproject.domain.enums;

public enum EGender {
	MALE("male"),
	FEMALE("female"),
	OTHER("other");

	private String value;

	EGender(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public static EGender getByValue(String value) {
		for (EGender gender : values()) {
			if (gender.getValue().equalsIgnoreCase(value)) {
				return gender;
			}
		}
		throw new IllegalArgumentException("Không tìm thấy giá trị enum cho " + value);
	}
}
