package com.yn.printer.service.common.service;

import com.yn.printer.service.common.exception.YnError;
import com.yn.printer.service.common.exception.YnErrorException;
import com.yn.printer.service.modules.auth.entity.User;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 用户端 Controller基类
 *
 * @author huabiao
 * @create 2023/12/08  15:42
 **/
public class BaseUserService {

    /**
     * 用户端 token-登录令牌 在请求头中的名称
     */
    public static final String USER_TOKEN_HEADER_NAME = "User-Token";
    @Value("${file.urlPath}")
    public String urlPath;
    @Resource
    private HttpServletRequest request;

    /**
     * 获取请求
     *
     * @return HttpServletRequest
     */
    protected HttpServletRequest getRequest() {
        if (this.request == null) {
            // TODO：封装异常
            throw new YnErrorException(YnError.YN_100002);
        }
        return this.request;
    }

    /**
     * 获取用户token(登录状态下)
     *
     * @return token
     * @author huabiao
     * @create 2023/12/11  14:38
     */
    protected String getToken() {
        return this.getRequest().getHeader(USER_TOKEN_HEADER_NAME);
    }

    /**
     * 获取用户(登录状态下)
     *
     * @return User
     * @author huabiao
     * @create 2023/12/11  14:38
     */
    protected User getUser() {
        Object object = request.getAttribute(USER_TOKEN_HEADER_NAME);
        if (object == null) {
            throw new YnErrorException(YnError.YN_000000);
        }
        User user = (User) object;
        if (user == null) {
            throw new YnErrorException(YnError.YN_200001);
        }
        return user;
    }

    /**
     * 获取用户ID
     *
     * @return 用户ID
     * @author huabiao
     * @create 2023/12/11  14:42
     */
    protected Long getUserId() {
        return getUser().getId();
    }
}
