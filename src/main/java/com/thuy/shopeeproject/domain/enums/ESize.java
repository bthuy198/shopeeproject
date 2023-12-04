package com.thuy.shopeeproject.domain.enums;

public enum ESize {
	S("S"),
	M("M"),
	L("L"),
	XL("XL");
	
	private final String value;

	ESize(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
