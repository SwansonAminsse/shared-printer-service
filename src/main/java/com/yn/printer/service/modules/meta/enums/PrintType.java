package com.yn.printer.service.modules.meta.enums;

import com.yn.printer.service.modules.operation.enums.ConsumableType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PrintType {

  DocumentPrinting("文档打印"),
  PhotoPrinting("照片打印"),
  IdPrinting("证件照打印");

  private final String name;

  public String getName() {
    return name;
  }

  // 获取打印类型对应消耗纸类型
  public PaperType getPaperType() {
    return PrintType.DocumentPrinting.equals(this) ? PaperType.A4 : PaperType.Inches6;
  }

  // 获取打印类型对应耗材类型
  public ConsumableType getConsumableType(){
    return ConsumableType.C1;
  }

}
