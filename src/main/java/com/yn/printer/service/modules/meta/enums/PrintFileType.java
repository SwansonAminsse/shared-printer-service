package com.yn.printer.service.modules.meta.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PrintFileType {

  IMAGE("图片"),
  PDF("pdf");

  private final String name;

  public String getName() {
    return name;
  }
}
