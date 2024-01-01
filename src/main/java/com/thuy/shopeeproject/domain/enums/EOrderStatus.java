package com.thuy.shopeeproject.domain.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;

import com.thuy.shopeeproject.exceptions.CustomErrorException;

public enum EOrderStatus {
	ALL("All"),
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

	private static final Map<String, EOrderStatus> NAME_MAP = Stream.of(values())
			.collect(Collectors.toMap(EOrderStatus::toString, Function.identity()));

	public static EOrderStatus fromString(final String name) {
		EOrderStatus eOrderStatus = NAME_MAP.get(name);
		if (null == eOrderStatus) {
			return EOrderStatus.ALL;
		}
		return eOrderStatus;
	}

}
