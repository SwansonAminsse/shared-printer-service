package com.yn.printer.service.modules.meta.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PrintFaces {

  SINGLE("单面"),
  DOUBLE("双面");

  private final String name;

  public String getName() {
    return name;
  }
}
