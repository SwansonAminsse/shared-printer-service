package com.yn.printer.service.modules.orders.enums;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderPrintType {
	DOCUMENT("DOCUMENT"),
	PHOTO("PHOTO"),
	DOCUMENT_PHOTO("DOCUMENT_PHOTO");
	private final String value;
	public String getValue() {
		return value;
	}
}
