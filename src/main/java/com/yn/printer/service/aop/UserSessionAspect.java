package com.yn.printer.service.aop;

import com.yn.printer.service.common.consts.CacheConst;
import com.yn.printer.service.common.consts.HeaderConst;
import com.yn.printer.service.common.exception.YnError;
import com.yn.printer.service.common.exception.YnErrorException;
import com.yn.printer.service.interceptor.AuditInterceptor;
import com.yn.printer.service.modules.auth.entity.User;
import com.yn.printer.service.modules.auth.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 切面-验证User身份
 *
 * @author huabiao
 * @create 2023/12/08  15:42
 */
@Aspect
@Component
public class UserSessionAspect {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private UserRepository userRepository;

    @Pointcut("execution(public * com.yn.printer.service.modules.*.controller.user.*.*(..))")
    public void anyMethod() {
    }

    /**
     * 验证并返回用户信息
     *
     * @param joinPoint
     */
    @Before("anyMethod()")
    public void doAccessCheck(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 获取令牌
        String userToken = request.getHeader(HeaderConst.USER_TOKEN_HEADER_NAME);

        String token = "";
        if (StringUtils.isNotBlank(userToken)) {
            token = CacheConst.getTokenUser(userToken);
        }
        if (StringUtils.isBlank(token)) {
            throw new YnErrorException(YnError.YN_200001);
        }
        // 获取用户ID
        String userId = stringRedisTemplate.opsForValue().get(token);
        if (userId == null) {
            throw new YnErrorException(YnError.YN_200001);
        }
        User user = userRepository.findById(Long.valueOf(userId)).orElse(null);
        if (user == null) {
            throw new YnErrorException(YnError.YN_200001);
        }
        request.setAttribute(HeaderConst.USER_TOKEN_HEADER_NAME, user);
        AuditInterceptor.CURRENT_USER.set(user);
    }
}
