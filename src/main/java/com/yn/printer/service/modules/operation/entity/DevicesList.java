package com.yn.printer.service.modules.operation.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import com.yn.printer.service.modules.channel.entity.ChannelPartner;
import com.yn.printer.service.modules.operation.enums.DeviceStatus;
import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.meta.entity.Area;
import com.yn.printer.service.modules.meta.entity.XingHao;
import com.yn.printer.service.modules.operation.enums.Cene;
import com.yn.printer.service.modules.operation.enums.DeviceType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "OPERATION_DEVICES_LIST")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class DevicesList extends AuditableModel implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OPERATION_DEVICES_LIST_SEQ")
  @SequenceGenerator(
          name = "OPERATION_DEVICES_LIST_SEQ",
          sequenceName = "OPERATION_DEVICES_LIST_SEQ",
          allocationSize = 1)
  private Long id;

  // 设备编号
  private String code;

  // 设备名
  private String name;
  private String versionNumber;
  // 经度
  private BigDecimal jd = BigDecimal.ZERO;

  // 纬度
  private BigDecimal wd = BigDecimal.ZERO;

  // 平台结算比例
  private Integer platformRatio = 0;

  // 城市合伙人结算比例
  private Integer cityPartnerRatio = 0;

  // 合作伙伴结算比例
  private Integer partnersRatio = 0;

  // 终端服务商结算比例
  private Integer terminalMerchantsRatio = 0;

  // 设备状态
  private Boolean deviceStatus = Boolean.TRUE;

  // 保底额
  private BigDecimal minimumAmount = BigDecimal.ZERO;

  // 场租费
  private BigDecimal rentalFees = BigDecimal.ZERO;

  // 收入
  private BigDecimal income = BigDecimal.ZERO;
  // 设备类型
  @Enumerated(value = EnumType.STRING)
  private DeviceType deviceType;
  // 打印页数
  private Integer printCount = 0;

  // 异常原因
  private String abnormalReason;

  // 场地类型
  @Enumerated(value = EnumType.STRING)
  private Cene siteType;

  // 设备状态
  @Enumerated(value = EnumType.STRING)
  private DeviceStatus status = DeviceStatus.OFFLINE;

  // 定价分类
  @ManyToOne(
          fetch = FetchType.LAZY,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "fix_price")
  private FixPrice fixPrice;

  // 平台
  @ManyToOne(
          fetch = FetchType.LAZY,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "platform")
  private ChannelPartner platform;

  // 城市合伙人
  @ManyToOne(
          fetch = FetchType.LAZY,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "city_partner")
  private ChannelPartner cityPartner;

  // 合作伙伴
  @ManyToOne(
          fetch = FetchType.LAZY,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "partners")
  private ChannelPartner partners;

  // 终端服务商
  @ManyToOne(
          fetch = FetchType.EAGER,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "terminal_merchants")
  private ChannelPartner terminalMerchants;

  // 运维人员
  @ManyToOne(
          fetch = FetchType.EAGER,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "dev")
  private Member dev;

  // 绑定用户
  @OneToOne(
          fetch = FetchType.EAGER,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "bind_user")
  private Member bindUser;

  // 绑定时间
  private LocalDateTime bindTime;

  // 省
  @ManyToOne(
          fetch = FetchType.LAZY,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "province")
  private Area province;

  // 市
  @ManyToOne(
          fetch = FetchType.LAZY,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "city")
  private Area city;

  // 区县
  @ManyToOne(
          fetch = FetchType.LAZY,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "county")
  private Area county;

  // 街道
  @ManyToOne(
          fetch = FetchType.LAZY,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "street")
  private Area street;

  // 型号
  @ManyToOne(
          fetch = FetchType.EAGER,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "xh")
  private XingHao xh;

  // 详细地址
  private String address;

  private String attrs;

  @OneToMany(
          fetch = FetchType.LAZY,
          mappedBy = "device",
          cascade = CascadeType.ALL,
          orphanRemoval = true)
  private List<PaperTable> paperNumber;

  @OneToMany(
          fetch = FetchType.LAZY,
          mappedBy = "device",
          cascade = CascadeType.ALL,
          orphanRemoval = true)
  private List<ConsumablesValue> consumablesValue;

  @JoinColumn(name ="created_on")
  private LocalDateTime createdOn;

}
