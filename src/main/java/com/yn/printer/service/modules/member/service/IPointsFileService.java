package com.yn.printer.service.modules.member.service;

import java.math.BigDecimal;
import com.yn.printer.service.modules.member.entity.Member;

public interface IPointsFileService {
    void creatAddPointsFile(BigDecimal amount, Member member);
}
