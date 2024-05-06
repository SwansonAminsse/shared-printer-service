package com.yn.printer.service.modules.member.repository;

import com.yn.printer.service.modules.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {

    Member findByOpenId(String openid);

    Member findByPhoneNumber(String phoneNumber);
    Member findFirstByPhoneNumber(String phoneNumber);

    long countByStatus(boolean b);
}

