package com.yn.printer.service.modules.channel.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OperatingModel {

	Lease("租聘"),
	Investment("投资"),
	Guarantee("保底");

	private final String name;

	public String getName() {
		return name;
	}
}
