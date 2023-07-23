package com.classroomassistant.util;

import org.springframework.stereotype.Component;

import static com.classroomassistant.util.ValidationUtil.*;

/**
 * @ClassName: RegExpUtil
 * @author: 赵容庆
 * @date: 2022年09月23日 9:43
 * @Description: TODO
 */

@Component
public class RegExpUtil {

    public static boolean matchEmail(String email) {
        return email.matches(EMAIL) || email.matches(QQ_EMAIL) || email.matches(WANGYI_EMAIL);
    }

    public static boolean matchUsername(String username) {
        return username.matches(USER_NAME);
    }

    public static boolean matchPassword(String password) {
        return password.matches(PWD_REGEXP);
    }

    public static boolean matchesTelephone(String telephone){
        return telephone.matches(PHONE_REGEXP);
    }


}
