package com.classroomassistant.util;

import com.classroomassistant.pojo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


/**
 * 用户工具类
 *
 * @author zrq
 * @date 2022/12/2
 */
@Component
@Slf4j
public class UserUtils {

    /**
     * 获取当前登录用户
     *
     * @return 用户登录信息
     */
    public static LoginUser getLoginUser() {
        return (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 获取当前用户id
     *
     * @return 但前用户id
     */
    public static Integer getUserId(){
        return getLoginUser().getUser().getStudentId();
    }

    /**
     * 获取用户身份
     *
     * @return 用户身份
     */
    public static Integer getIdentity(){
        return getLoginUser().getUser().getIdentity();
    }

}
