package com.yn.printer.service.modules.member.repository;

import com.yn.printer.service.modules.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {

    Member findByOpenId(String openid);

    Member findByPhoneNumber(String phoneNumber);

    Member findFirstByPhoneNumber(String phoneNumber);

    long countByStatus(boolean b);

    @Query("SELECT COUNT(m) FROM Member m WHERE m.joiningDate = :currentDate")
    long countByJoiningDate(@Param("currentDate") LocalDate currentDate);

}

