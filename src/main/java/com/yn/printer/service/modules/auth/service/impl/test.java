package com.yn.printer.service.modules.auth.service.impl;

import com.yn.printer.service.modules.auth.entity.User;
import com.yn.printer.service.modules.auth.repository.UserRepository;
import com.yn.printer.service.modules.auth.util.ShiroUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class test {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void adduser() {
        User user = new User();
        user.setCode("wrh");
        user.setName("wang");
        ShiroUtil shiroUtil = new ShiroUtil();
        String encryptedPassword = shiroUtil.encrypt("123456");
        user.setPassword(encryptedPassword);
        userRepository.save(user);
    }
}
