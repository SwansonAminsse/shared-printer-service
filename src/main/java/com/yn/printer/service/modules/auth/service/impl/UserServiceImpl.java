package com.yn.printer.service.modules.auth.service.impl;

import com.yn.printer.service.common.consts.CacheConst;
import com.yn.printer.service.common.exception.YnError;
import com.yn.printer.service.common.exception.YnErrorException;
import com.yn.printer.service.modules.auth.dto.LoginDTO;
import com.yn.printer.service.modules.auth.dto.UserAddDTO;
import com.yn.printer.service.modules.auth.dto.UserUpdateDTO;
import com.yn.printer.service.modules.auth.entity.User;
import com.yn.printer.service.modules.auth.repository.UserRepository;
import com.yn.printer.service.modules.auth.service.UserService;
import com.yn.printer.service.modules.auth.util.ShiroUtil;
import com.yn.printer.service.modules.auth.vo.LoginVO;
import com.yn.printer.service.modules.auth.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现
 *
 * @author huabiao
 * @create 2023/12/11  10:29
 **/
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        LoginVO loginVO = new LoginVO();
        // 判断账户是否存在
       // User user = userRepository.findFirstByCodeOrPhone(loginDTO.getAccount(), loginDTO.getAccount());
       User user = userRepository.findFirstByCode(loginDTO.getAccount());
        if (user == null) {
            throw new YnErrorException(YnError.YN_200002);
        }
        // 验证密码
        ShiroUtil shiroUtil = new ShiroUtil();
        if (!shiroUtil.match(loginDTO.getPassword(), user.getPassword())) {
            throw new YnErrorException(YnError.YN_200003);
        }
        // 生成token
        String token = UUID.randomUUID().toString();
        // 添加token数据
        stringRedisTemplate.opsForValue().set(CacheConst.getTokenUser(token), user.getId().toString(), 7, TimeUnit.DAYS);
        loginVO.setToken(token);
        loginVO.setAccount(user.getCode());
        return loginVO;
    }

    @Override
    public Page<UserVO> page(String keyword, Pageable pageable) {
        Specification<User> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(keyword)) {
                List<Predicate> keywordPredicateList = new ArrayList<>();
                keywordPredicateList.add(criteriaBuilder.like(root.get("code"), "%" + keyword + "%"));
                keywordPredicateList.add(criteriaBuilder.like(root.get("name"), "%" + keyword + "%"));
                predicateList.add(criteriaBuilder.or(keywordPredicateList.toArray(new Predicate[0])));
            }
            query.where(predicateList.toArray(new Predicate[predicateList.size()]));
            query.orderBy(criteriaBuilder.desc(root.get("createdOn")));
            return query.getRestriction();
        };
        Page<User> userPage = userRepository.findAll(specification, pageable);
        List<UserVO> userVOList = new ArrayList<>();
        for (User user : userPage.getContent()) {
            UserVO userVO = new UserVO();
            userVO.setId(user.getId());
            userVO.setCode(user.getCode());
            userVO.setName(user.getName());
            userVOList.add(userVO);
        }
        return new PageImpl(userVOList, pageable, userPage.getTotalElements());
    }

    @Override
    public UserVO userDetail(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new YnErrorException(YnError.YN_200002);
        }
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setCode(user.getCode());
        userVO.setName(user.getName());
        return userVO;
    }

    @Override
    public void addUser(UserAddDTO userAddDTO) {
        User user = new User();
        user.setCode(userAddDTO.getAccount());
        // 密码加密
        ShiroUtil shiroUtil = new ShiroUtil();
        user.setPassword(shiroUtil.encrypt(userAddDTO.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(userUpdateDTO.getId()).orElse(null);
        if (user == null) {
            throw new YnErrorException(YnError.YN_200002);
        }
        user.setName(userUpdateDTO.getName());
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}