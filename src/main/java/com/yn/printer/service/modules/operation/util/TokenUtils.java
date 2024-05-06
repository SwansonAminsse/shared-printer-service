package com.yn.printer.service.modules.operation.util;

import com.yn.printer.service.common.consts.CacheConst;
import com.yn.printer.service.common.consts.HeaderConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class TokenUtils {

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public TokenUtils(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public String getPhoneNumber(HttpServletRequest request) {
        String token = request.getHeader(HeaderConst.MEMBER_TOKEN_HEADER_NAME);
        return stringRedisTemplate.opsForValue().get(CacheConst.getPhoneNumberKey(token));
    }
}