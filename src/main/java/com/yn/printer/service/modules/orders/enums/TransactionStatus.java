package com.yn.printer.service.modules.orders.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TransactionStatus {

  INIT("初始化"),
  IN_PROGRESS("进行中"),
  COMPLETE("已完成"),
  CANCELED("已取消"),
  ABNORMAL("订单异常");

  private final String name;

  public String getName() {
    return name;
  }
}
