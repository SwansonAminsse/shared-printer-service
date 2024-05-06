package com.yn.printer.service.modules.orders.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PayStatus {

  PAID("已付款"),
  UN_PAID("未付款"),
  REFUNDED("已退款");

  private final String name;

  public String getName() {
    return name;
  }
}
