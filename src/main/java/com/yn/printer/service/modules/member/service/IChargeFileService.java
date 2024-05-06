package com.yn.printer.service.modules.member.service;

import com.yn.printer.service.modules.member.entity.Member;

import java.math.BigDecimal;

public interface IChargeFileService {

    Boolean creatAddChargeFile(BigDecimal amount, Member member);

    Boolean lowAddChargeFile(BigDecimal amount, Member member);
}
