package com.yn.printer.service.modules.meta.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PrintColor {
  BW("黑白"),
  COLOR("彩色");

  private final String name;

  public String getName() {
    return name;
  }
}
