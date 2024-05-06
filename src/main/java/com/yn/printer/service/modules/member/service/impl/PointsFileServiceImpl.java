package com.yn.printer.service.modules.member.service.impl;


import com.yn.printer.service.interceptor.AuditInterceptor;
import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.member.repository.MemberRepository;
import com.yn.printer.service.modules.member.repository.PointsFileRepository;
import com.yn.printer.service.modules.member.service.IPointsFileService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yn.printer.service.modules.member.entity.PointsFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Slf4j
@Service
public class PointsFileServiceImpl implements IPointsFileService {
    @Autowired
    PointsFileRepository pointsFileRepository;
    @Autowired
    MemberRepository memberRepository;
    @Override
    public void creatAddPointsFile(BigDecimal amount, Member member) {
         double   Magnification = 100;
        PointsFile pointsFile = new PointsFile();
        pointsFile.setPoints(amount.multiply(new BigDecimal(Magnification)).setScale(0, RoundingMode.DOWN).intValue());
        if (pointsFile.getPoints() < 1) {
            return;
        }
        pointsFile.setExpires(LocalDateTime.now().plusYears(1));
        pointsFile.setIncreasing(true);
        pointsFile.setMember(member);
        pointsFileRepository.save(pointsFile);
    }

    private Integer sumPoints() {
        Integer increasingTotal = pointsFileRepository.sumOfActiveIncreasingPoints(AuditInterceptor.CURRENT_MEMBER.get());
        Integer lowTotal = pointsFileRepository.sumOfLowPoints(AuditInterceptor.CURRENT_MEMBER.get());
        return increasingTotal - lowTotal;
    }
}