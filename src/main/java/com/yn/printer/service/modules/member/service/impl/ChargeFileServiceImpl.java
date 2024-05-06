package com.yn.printer.service.modules.member.service.impl;

import com.yn.printer.service.interceptor.AuditInterceptor;
import com.yn.printer.service.modules.member.entity.ChargeFile;
import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.member.entity.PointsFile;
import com.yn.printer.service.modules.member.repository.ChargeFileRepository;
import com.yn.printer.service.modules.member.service.IChargeFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Slf4j
@Service
public class ChargeFileServiceImpl implements IChargeFileService {

 @Autowired
 ChargeFileRepository chargeFileRepository;







        @Override
        public Boolean creatAddChargeFile(BigDecimal amount, Member member){
          try{  ChargeFile chargeFile = new ChargeFile();
            chargeFile.setRefillAmount(amount);
            chargeFile.setTime(LocalDateTime.now());
            chargeFile.setIncreasing(true);
            chargeFile.setMember(member);
            chargeFileRepository.save(chargeFile);
            return true;}
          catch (Exception e){
              log.error("充值失败",e);
              return false;
          }
        };

    @Override
    public Boolean lowAddChargeFile(BigDecimal amount, Member member){
        try{ ChargeFile chargeFile = new ChargeFile();
        chargeFile.setRefillAmount(amount);
        chargeFile.setTime(LocalDateTime.now());
        chargeFile.setIncreasing(false);
        chargeFile.setMember(member);
        chargeFileRepository.save(chargeFile);
            return true;}catch (Exception e){
            log.error("充值失败",e);
            return false;
        }
    };

    private BigDecimal sumCharge() {
        BigDecimal increasingTotal = chargeFileRepository.sumOfActiveIncreasingPoints(AuditInterceptor.CURRENT_MEMBER.get());
        BigDecimal lowTotal = chargeFileRepository.sumOfLowPoints(AuditInterceptor.CURRENT_MEMBER.get());
        return increasingTotal.subtract(lowTotal);
    }
}