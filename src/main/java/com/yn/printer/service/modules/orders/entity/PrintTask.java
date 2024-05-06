package com.yn.printer.service.modules.orders.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import com.yn.printer.service.modules.meta.entity.MetaFile;
import com.yn.printer.service.modules.meta.enums.PrintColor;
import com.yn.printer.service.modules.meta.enums.PrintDirection;
import com.yn.printer.service.modules.meta.enums.PrintFaces;
import com.yn.printer.service.modules.meta.enums.PrintType;
import com.yn.printer.service.modules.operation.entity.DevicesList;
import com.yn.printer.service.modules.orders.enums.PrintTaskStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "ORDERS_PRINT_TASK")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class PrintTask extends AuditableModel {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDERS_PRINT_TASK_SEQ")
  @SequenceGenerator(
          name = "ORDERS_PRINT_TASK_SEQ",
          sequenceName = "ORDERS_PRINT_TASK_SEQ",
          allocationSize = 1)
  private Long id;

  // 任务号
  private String code;

  // 设备
  @ManyToOne(
          fetch = FetchType.EAGER,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "device")
  private DevicesList device;

  // 订单
  @ManyToOne(
          fetch = FetchType.EAGER,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "orders")
  private OrderManagement orders;

  // 打印状态
  @Enumerated(EnumType.STRING)
  private PrintTaskStatus printTaskStatus;

  // 打印类型
  @Enumerated(EnumType.STRING)
  private PrintType printType;

  // 打印颜色
  @Enumerated(EnumType.STRING)
  private PrintColor printingColor;

  // 打印面数
  @Enumerated(EnumType.STRING)
  private PrintFaces printingFaces;

  // 打印方向
  @Enumerated(EnumType.STRING)
  private PrintDirection printDirection;

  // 打印份数
  private Integer copies = 0;

  // 消耗纸张数
  private Integer paperConsume = 0;

  // 总页数
  private Integer pageSize = 0;

  // 完成页数
  private Integer pageComplete = 0;

  // 源文件名
  private String sourceFileName;

  // 中断原因
  private String interruptReason;

  // 源文件
  @OneToOne(
          fetch = FetchType.LAZY,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "source_file")
  private MetaFile sourceFile;

  // 转码文件
  @OneToOne(
          fetch = FetchType.EAGER,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "transcoding_file")
  private MetaFile transcodingFile;

  // 任务推送时间
  private LocalDateTime pushTime;

  // 任务完成时间
  private LocalDateTime endTime;

  private String attrs;

}
