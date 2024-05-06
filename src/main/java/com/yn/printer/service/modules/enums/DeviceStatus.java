package com.yn.printer.service.modules.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DeviceStatus {

  NOT_ACTIVE("NOT_ACTIVE", "未激活"), // 设备出厂时默认状态
  ONLINE("ONLINE", "在线"), // 设备空闲且可以处理打印任务时的状态
  OFFLINE("OFFLINE", "离线"), // 客户端失去连接
  RUN("RUN", "运行"), // 设备正在打印文件时的状态
  ABNORMAL("ABNORMAL", "异常"), // 设备出现异常时的状态 例: 设备缺纸/卡纸/缺墨水
  STOP("STOP", "停用"); // 后台停用设备时的状态

  private final String value;
  private final String name;

  public String getValue() {
    return value;
  }

  public String getName() {
    return name;
  }

}
