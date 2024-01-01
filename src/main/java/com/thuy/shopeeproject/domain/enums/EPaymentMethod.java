package com.thuy.shopeeproject.domain.enums;

public enum EPaymentMethod {
	CASH_ON_DELIVERY("Cash on Delivery"),
	CREDIT_CARD("Credit Card"),
	BANK_TRANSFER("Bank Transfer");

	private String value;

	EPaymentMethod(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
