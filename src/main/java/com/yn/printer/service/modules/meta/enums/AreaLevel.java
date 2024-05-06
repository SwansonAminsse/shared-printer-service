package com.yn.printer.service.modules.meta.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AreaLevel {

	Province("省"),
	Municipal("市"),
	County("县"),
	Street("街道");

	private final String name;

	public String getName() {
		return name;
	}
}
