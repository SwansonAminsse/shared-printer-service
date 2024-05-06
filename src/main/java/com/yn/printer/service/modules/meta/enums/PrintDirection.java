package com.yn.printer.service.modules.meta.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PrintDirection {

  VERTICAL("垂直"),
  LEVEL("水平");

  private final String name;

  public String getName() {
    return name;
  }
}
