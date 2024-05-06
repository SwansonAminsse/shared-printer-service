package com.yn.printer.service.modules.orders.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PrintTaskStatus {
  TO_BE_PRINTED("待打印"),
  TO_BE_PUSH("待推送"),
  PUSHED("已推送"),
  PRINTING("打印中"),
  COMPLETE("已完成"),
  CANCELED("已取消"),
  INTERRUPT("已中断");

  private final String name;

  public String getName() {
    return name;
  }
}
