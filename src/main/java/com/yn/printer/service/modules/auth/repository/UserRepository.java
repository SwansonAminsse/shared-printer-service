package com.yn.printer.service.modules.auth.repository;

import com.yn.printer.service.modules.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * 用户Repository
 *
 * @author huabiao
 * @create 2023/12/08  15:42
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * 用户-根据登录名或者手机号查询
     *
     * @param code  登录名
     * @param phone 手机号
     * @return 用户
     * @author huabiao
     * @create 2023/12/11  10:30
     */
    //User findFirstByCodeOrPhone(String code, String phone);
    User findFirstByCode(String code);
}
