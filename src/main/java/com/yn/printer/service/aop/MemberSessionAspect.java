package com.yn.printer.service.aop;

import com.yn.printer.service.common.consts.CacheConst;
import com.yn.printer.service.common.consts.HeaderConst;
import com.yn.printer.service.common.exception.YnError;
import com.yn.printer.service.common.exception.YnErrorException;
import com.yn.printer.service.interceptor.AuditInterceptor;
import com.yn.printer.service.modules.auth.entity.User;
import com.yn.printer.service.modules.auth.repository.UserRepository;
import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.member.repository.MemberRepository;
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
 * 切面-验证Member身份
 *
 * @author : Jonas Chan
 * @since : 2023/12/14 15:25
 */
@Aspect
@Component
public class MemberSessionAspect {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private MemberRepository memberRepository;

    @Pointcut("execution(public * com.yn.printer.service.modules.*.controller.auth.*.*(..))")
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
        String memberToken = request.getHeader(HeaderConst.MEMBER_TOKEN_HEADER_NAME);
        String token = "";
        if (StringUtils.isNotBlank(memberToken)) {
            token = CacheConst.getTokenMember(memberToken);
        }
        if (StringUtils.isBlank(token)) {
            throw new YnErrorException(YnError.YN_200001);
        }

        String userId = stringRedisTemplate.opsForValue().get(token);
        if (userId == null) {
            throw new YnErrorException(YnError.YN_200001);
        }
        Member member = memberRepository.findById(Long.valueOf(userId)).orElse(null);
        if (member == null) {
            throw new YnErrorException(YnError.YN_200001);
        }
        request.setAttribute(HeaderConst.USER_TOKEN_HEADER_NAME, member);
        AuditInterceptor.CURRENT_MEMBER.set(member);
    }
}
