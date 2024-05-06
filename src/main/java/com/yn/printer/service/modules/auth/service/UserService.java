package com.yn.printer.service.modules.auth.service;

import com.yn.printer.service.modules.auth.dto.LoginDTO;
import com.yn.printer.service.modules.auth.dto.UserAddDTO;
import com.yn.printer.service.modules.auth.dto.UserUpdateDTO;
import com.yn.printer.service.modules.auth.vo.LoginVO;
import com.yn.printer.service.modules.auth.vo.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 用户服务
 *
 * @author huabiao
 * @create 2023/12/11  10:26
 **/
public interface UserService {

    /**
     * 用户-登录
     *
     * @param loginDTO 登录DTO
     * @return 登录VO
     * @author huabiao
     * @create 2023/12/11  10:28
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 用户-分页查询
     *
     * @param keyword  关键字
     * @param pageable 分页参数
     * @return
     * @description
     * @author huabiao
     * @create 2023/12/11  14:29
     */
    Page<UserVO> page(String keyword, Pageable pageable);

    /**
     * 用户-详情
     *
     * @param id 用户ID
     * @return UserVO
     * @description
     * @author huabiao
     * @create 2023/12/11  10:52
     */
    UserVO userDetail(Long id);

    /**
     * 用户-新增
     *
     * @param userAddDTO 用户新增DTO
     * @author huabiao
     * @create 2023/12/11  10:57
     */
    void addUser(UserAddDTO userAddDTO);

    /**
     * 用户-修改
     *
     * @param userUpdateDTO 用户修改DTO
     * @author huabiao
     * @create 2023/12/11  11:03
     */
    void updateUser(UserUpdateDTO userUpdateDTO);

    /**
     * 用户-删除
     *
     * @param id 用户ID
     * @author huabiao
     * @create 2023/12/11  11:05
     */
    void deleteUser(Long id);
}
