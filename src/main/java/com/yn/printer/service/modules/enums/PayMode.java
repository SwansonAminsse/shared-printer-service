package com.yn.printer.service.modules.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PayMode {
  TZ_PAY("TZ_PAY", "天章支付"),
  WX_NATIVE_PAY("WX_PAY", "微信本地支付"),
  WX_JS_PAY("WX_JS_PAY", "微信小程序支付"),

  YU_E_PAY("YU_E_PAY", "余额支付");

  private final String value;
  private final String name;

  public String getValue() {
    return value;
  }

  public String getName() {
    return name;
  }
}
