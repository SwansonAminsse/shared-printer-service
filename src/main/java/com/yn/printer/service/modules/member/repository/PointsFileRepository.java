package com.yn.printer.service.modules.member.repository;

import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.member.entity.PointsFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PointsFileRepository extends JpaRepository<PointsFile, Long>, JpaSpecificationExecutor<PointsFile> {

    @Query("SELECT SUM(pf.points) FROM PointsFile pf WHERE pf.expires > CURRENT_TIMESTAMP AND pf.increasing = true AND pf.member = :member")
    Integer sumOfActiveIncreasingPoints(@Param("member") Member member);
    @Query("SELECT SUM(pf.points) FROM PointsFile pf WHERE pf.low = true AND pf.member = :member")
    Integer sumOfLowPoints(@Param("member") Member member);


}
