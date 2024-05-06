package com.yn.printer.service.modules.meta.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "META_THIRD_PARTY_VOUCHERS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ThirdPartyVouchers extends AuditableModel implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "META_THIRD_PARTY_VOUCHERS_SEQ")
  @SequenceGenerator(
          name = "META_THIRD_PARTY_VOUCHERS_SEQ",
          sequenceName = "META_THIRD_PARTY_VOUCHERS_SEQ",
          allocationSize = 1)
  private Long id;

  // 阿里云证件照制作-AppCode
  private String aliIdPhotoAppCode;

  private String aliyunMattingAppCode;

  private String email;

  private String emailAuthCode;
  private String tentcentMapKey;

}
