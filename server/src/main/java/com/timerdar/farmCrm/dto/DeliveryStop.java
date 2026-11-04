package com.timerdar.farmCrm.dto;

public record DeliveryStop(
		long id,
		String name,
		String address,
		String phone,
		int deliveryOrderNumber,
		int deliverySum,
		int openCount) {
}
