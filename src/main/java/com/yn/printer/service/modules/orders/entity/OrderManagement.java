package com.yn.printer.service.modules.orders.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import com.yn.printer.service.modules.orders.enums.PayMode;
import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.operation.entity.DevicesList;
import com.yn.printer.service.modules.orders.enums.OrderPrintType;
import com.yn.printer.service.modules.orders.enums.OrderType;
import com.yn.printer.service.modules.orders.enums.PayStatus;
import com.yn.printer.service.modules.orders.enums.TransactionStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "ORDERS_ORDER_MANAGEMENT")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class OrderManagement extends AuditableModel {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDERS_ORDER_MANAGEMENT_SEQ")
  @SequenceGenerator(
          name = "ORDERS_ORDER_MANAGEMENT_SEQ",
          sequenceName = "ORDERS_ORDER_MANAGEMENT_SEQ",
          allocationSize = 1)
  private Long id;

  // 订单号
  private String code;

  // 交易单号
  private String tradeNo;

  // 下单时间
  private LocalDateTime orderDate;

  private LocalDateTime updatedOn;

  private LocalDateTime createdOn;

  // 订单金额
  private BigDecimal orderAmount = BigDecimal.ZERO;

  // 支付时间
  private LocalDateTime payDate;

  // 支付金额
  private BigDecimal paymentAmount = BigDecimal.ZERO;

  // 订单类型
  @Enumerated(value = EnumType.STRING)
  private OrderType orderType;

  // 商户号
  private String mch;

  // 第三方流水号
  private String thirdPartySn;

  // 支付码
  private String qrCode;

  // 异常原因
  private String abnormalReason;

  // 交易状态
  @Enumerated(value = EnumType.STRING)
  private TransactionStatus transactionStatus;

  // 支付状态
  @Enumerated(value = EnumType.STRING)
  private PayStatus payStatus;

  // 打印设备
  @ManyToOne(
          fetch = FetchType.EAGER,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "device")
  private DevicesList device;

  // 付款人
  @ManyToOne(
          fetch = FetchType.LAZY,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "payer")
  private Member payer;

  // 下单人
  @ManyToOne(
          fetch = FetchType.LAZY,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "orderer")
  private Member orderer;

  // 打印任务
  @OneToMany(
          fetch = FetchType.LAZY,
          mappedBy = "orders",
          cascade = CascadeType.ALL,
          orphanRemoval = true)
  private List<PrintTask> printTask;

  // 微信支付链接
  private String wxPayLink;

  // 微信小程序预支付订单标识
  private String wxPrepayId;

  // 支付方式
  @Enumerated(EnumType.STRING)
  private PayMode payMode;


  private String attrs;

  @Enumerated(EnumType.STRING)
  private OrderPrintType orderPrintType;

}
