package com.yn.printer.service.modules.meta.enums;

import com.yn.printer.service.modules.operation.entity.PaperTable;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public enum PaperType {

  A4("A4纸"),
  Photo("6寸长卷相纸"),
  Inches6("6寸相纸");

  private final String name;

  public String getName() {
    return name;
  }

  public static PaperType getActualPaperType(List<PaperTable> paperNumber) {
    for (PaperTable paperTable : paperNumber) {
      if ("6寸长卷相纸".equals(paperTable.getName().getName())) {
        return PaperType.Photo;
      }
    }
    return Inches6;
  }

}
