package com.yn.printer.service.modules.operation.enums;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OperationsType {

  AddingPaper("加纸"),
  ReplacingConsumables("换耗材"),
  Other("其他运维");

  private final String name;

  public String getName() {
    return name;
  }

}
