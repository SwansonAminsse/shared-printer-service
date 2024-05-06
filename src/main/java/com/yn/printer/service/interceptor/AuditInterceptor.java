package com.yn.printer.service.interceptor;

import com.yn.printer.service.common.annotation.Sequence;
import com.yn.printer.service.common.consts.HeaderConst;
import com.yn.printer.service.common.entity.AuditableModel;
import com.yn.printer.service.common.entity.Model;
import com.yn.printer.service.common.util.SequenceUtil;
import com.yn.printer.service.common.util.SpringContextHolder;
import com.yn.printer.service.modules.auth.entity.User;
import com.yn.printer.service.modules.auth.repository.UserRepository;
import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.meta.entity.MetaSequence;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 审计拦截器
 *
 * @author huabiao
 * @create 2023/12/8  16:56
 **/
@Component
public class AuditInterceptor extends EmptyInterceptor {

    public static final ThreadLocal<User> CURRENT_USER = new ThreadLocal<>();
    public static final ThreadLocal<Member> CURRENT_MEMBER = new ThreadLocal<>();

    private static final String UPDATED_BY = "updatedBy";
    private static final String UPDATED_ON = "updatedOn";
    private static final String CREATED_BY = "createdBy";
    private static final String CREATED_ON = "createdOn";

    /**
     * 事务开始后
     *
     * @param transaction 事务
     * @author huabiao
     * @create 2023/12/11  10:02
     */
    @Override
    public void afterTransactionBegin(Transaction transaction) {
        try {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (servletRequestAttributes != null) {
                HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
                CURRENT_USER.set((User) httpServletRequest.getAttribute(HeaderConst.USER_TOKEN_HEADER_NAME));
                CURRENT_MEMBER.set((Member) httpServletRequest.getAttribute(HeaderConst.MEMBER_TOKEN_HEADER_NAME));
            }
        } catch (Exception exception) {
            // TODO 暂不输出错误
        }
    }

    /**
     * 事务完成后
     *
     * @param transaction 事务
     * @author huabiao
     * @create 2023/12/11  10:02
     */
    @Override
    public void afterTransactionCompletion(Transaction transaction) {
        CURRENT_USER.remove();
        CURRENT_MEMBER.remove();
    }


    /**
     * 事务完成前
     *
     * @param transaction 事务
     * @author huabiao
     * @create 2023/12/11  10:02
     */
    @Override
    public void beforeTransactionCompletion(Transaction transaction) {
    }

    /**
     * 更新对象时
     *
     * @param entity        实体
     * @param id            ID
     * @param currentState  当前状态
     * @param previousState 之前状态
     * @param propertyNames 属性名称
     * @param types         类型
     * @return true:审计处理完成;false:审计处理失败
     * @author huabiao
     * @create 2023/12/11  10:02
     */
    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        if (!(entity instanceof AuditableModel)) {
            return false;
        }
        final User user = this.getUser();
        for (int i = 0; i < propertyNames.length; i++) {
            if (!canUpdate(entity, propertyNames[i], previousState[i], currentState[i])) {
                throw new PersistenceException(String.format("You can't update: %s#%s, values (%s=%s)", entity.getClass().getName(), id, propertyNames[i], currentState[i]));
            }
            if (UPDATED_ON.equals(propertyNames[i])) {
                currentState[i] = LocalDateTime.now();
            }
            if (UPDATED_BY.equals(propertyNames[i]) && user != null) {
                currentState[i] = user;
            }
        }
        return true;
    }

    /**
     * 保存对象时
     *
     * @param entity        实体
     * @param id            ID
     * @param state         状态
     * @param propertyNames 属性名称
     * @param types         类型
     * @return true:审计处理完成;false:审计处理失败
     * @author huabiao
     * @create 2023/12/11  10:19
     */
    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        boolean changed = updateSequence(entity, propertyNames, state);
        if (!(entity instanceof AuditableModel)) {
            return changed;
        }
        final User user = this.getUser();
        for (int i = 0; i < propertyNames.length; i++) {
            if (state[i] != null) {
                continue;
            }
            if (CREATED_ON.equals(propertyNames[i]) && state[i] == null) {
                state[i] = LocalDateTime.now();
            }
            if (CREATED_BY.equals(propertyNames[i]) && user != null) {
                state[i] = user;
            }
        }
        return true;
    }

    /**
     * 获取用户
     *
     * @return 当前用户
     * @description 若不存在则获取超级管理员
     * @author huabiao
     * @create 2023/12/11  10:20
     */
    private User getUser() {
        User user = CURRENT_USER.get();
        Long userId = 1L;
        if (user != null) {
            userId = user.getId();
        }
        UserRepository userRepository = SpringContextHolder.getBean(UserRepository.class);
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.get();
    }

    /**
     * 删除对象时
     *
     * @param entity        实体
     * @param id            ID
     * @param state         状态
     * @param propertyNames 属性名称
     * @param types         类型
     * @author huabiao
     * @create 2023/12/11  10:20
     */
    @Override
    public void onDelete(
            Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (!canDelete(entity)) {
            throw new PersistenceException(String.format("You can't delete: %s#%s", entity.getClass().getName(), id));
        }
    }

    /**
     * 更新序列
     *
     * @param entity 实体
     * @param names  名称
     * @param state  状态
     * @return true:更新成功;false:更新失败
     * @description
     * @author huabiao
     * @create 2023/12/11  10:02
     */
    private boolean updateSequence(Object entity, String[] names, Object[] state) {
        if ((entity instanceof MetaSequence) || !(entity instanceof Model)) {
            return false;
        }
        Class<?> entityClass = entity.getClass();
        Field[] declaredFields = entityClass.getDeclaredFields();
        boolean updated = false;
        for (int i = 0; i < declaredFields.length; i++) {
            if (state[i] != null) {
                continue;
            }
            boolean annotationPresent = declaredFields[i].isAnnotationPresent(Sequence.class);
            if (annotationPresent) {
                state[i] = SequenceUtil.nextValue(declaredFields[i].getName());
                updated = true;
            }
        }
        return updated;
    }

    /**
     * 是否能否更新
     *
     * @param entity        实体
     * @param propertyName  属性名称
     * @param previousState 之前状态
     * @param currentState  当前状态
     * @return true:能;false:否
     * @author huabiao
     * @create 2023/12/11  10:21
     */
    private boolean canUpdate(Object entity, String propertyName, Object previousState, Object currentState) {
        return true;
    }

    /**
     * 是否能够删除
     *
     * @param entity 实体
     * @return true:能;false:否
     * @author huabiao
     * @create 2023/12/11  10:02
     */
    private boolean canDelete(Object entity) {
        return true;
    }
}
