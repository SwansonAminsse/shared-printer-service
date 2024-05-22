package com.yn.printer.service.modules.channel.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ChannelType {

	PrimaryChannel("B"),
	SecondaryChannel("b商"),
	TerminalChannel("R商"),
	TZ("TZ");

	private final String name;

	public String getName() {
		return name;
	}
}
