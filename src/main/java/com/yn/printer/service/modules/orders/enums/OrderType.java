package com.yn.printer.service.modules.orders.enums;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderType {
  PRINT("打印订单"),
  RECHARGE("充值订单");

  private final String name;

  public String getName() {
    return name;
  }
}
