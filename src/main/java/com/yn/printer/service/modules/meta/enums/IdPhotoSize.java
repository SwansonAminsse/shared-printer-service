package com.yn.printer.service.modules.meta.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum IdPhotoSize {

  M1("一寸", 413, 295, 1),
  S1("小一寸", 378, 260, 2),
  L1("大一寸", 567, 390, 3),
  M2("二寸", 579, 413, 4),
  S2("小二寸", 531, 413, 5),
  L2("大二寸", 1, 1, 6),
  M3("三寸", 992, 650, 19);

  private final String name;
  private final Integer height;
  private final Integer width;

  // 阿里云证件照制作接口参数定义: 规格
  private final Integer spec;

  public String getName() {
    return name;
  }

  public Integer getHeight() {
    return height;
  }

  public Integer getWidth() {
    return width;
  }

  public Integer getSpec() {
    return spec;
  }
}
