package com.thuy.shopeeproject.domain.enums;

public enum EGender {
	MALE("male"),
	FEMALE("female"),
	OTHER("other");

	private String value;
	
	EGender(String value) {
		this.value = value;
	}
}
