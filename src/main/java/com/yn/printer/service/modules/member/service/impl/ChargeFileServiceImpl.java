package com.yn.printer.service.modules.member.service.impl;

import com.yn.printer.service.common.exception.YnError;
import com.yn.printer.service.common.exception.YnErrorException;
import com.yn.printer.service.interceptor.AuditInterceptor;
import com.yn.printer.service.modules.member.entity.ChargeFile;
import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.member.entity.PointsFile;
import com.yn.printer.service.modules.member.repository.ChargeFileRepository;
import com.yn.printer.service.modules.member.repository.MemberRepository;
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

    @Autowired
    MemberRepository memberRepository;






        @Override
        public Boolean creatAddChargeFile(BigDecimal amount, Member member) {
            try {
                ChargeFile chargeFile = new ChargeFile();
                BigDecimal newInvestMoney = member.getInvestMoney() != null ? member.getInvestMoney().add(amount) : amount;
                BigDecimal newAccountBalance = member.getAccountBalance() != null ? member.getAccountBalance().add(amount) : amount;
                Integer newChargeTimes = member.getChargeTimes() != null ? member.getChargeTimes() + 1 : 1;
                member.setAccountBalance(newAccountBalance);
                member.setInvestMoney(newInvestMoney);
                member.setChargeTimes(newChargeTimes);
                chargeFile.setPayAmount(amount);
                chargeFile.setRefillAmount(amount);
                chargeFile.setTime(LocalDateTime.now());
                chargeFile.setIncreasing(true);
                chargeFile.setMember(member);
                memberRepository.save(member);
                chargeFileRepository.save(chargeFile);
                return true;
            } catch (Exception e) {
                throw new YnErrorException(YnError.YN_500006);
            }
        };

    @Override
    public Boolean lowAddChargeFile(BigDecimal amount, Member member){
        try {
            ChargeFile chargeFile = new ChargeFile();
            chargeFile.setRefillAmount(amount);
            chargeFile.setTime(LocalDateTime.now());
            chargeFile.setIncreasing(false);
            chargeFile.setMember(member);
            chargeFileRepository.save(chargeFile);
            return true;
        }
        catch (Exception e){
            throw new YnErrorException(YnError.YN_500007);
        }
    };

}