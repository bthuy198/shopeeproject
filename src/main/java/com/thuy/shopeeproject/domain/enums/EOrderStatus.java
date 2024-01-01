package com.thuy.shopeeproject.domain.enums;

public enum EOrderStatus {
	TO_PAY("To Pay"),
	TO_SHIP("To Ship"),
	TO_RECEIVE("To Receive"),
	COMPLETED("Completed"),
	CANCELLED("Cancelled"),
	RETURN_REFUND("Return/ Refund");

	private String value;

	EOrderStatus(String value) {
		// TODO Auto-generated constructor stub
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

}
