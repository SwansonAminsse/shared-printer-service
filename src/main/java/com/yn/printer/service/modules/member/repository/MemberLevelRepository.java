package com.yn.printer.service.modules.member.repository;

import com.yn.printer.service.modules.member.entity.MemberLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberLevelRepository extends JpaRepository<MemberLevel, Long>, JpaSpecificationExecutor<MemberLevel> {

}

