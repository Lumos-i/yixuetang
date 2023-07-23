package com.classroomassistant.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zrq
 * @ClassName UserDto
 * @date 2023/1/11 23:56
 * @Description TODO
 */
@Data
public class UserDto implements Serializable {
    private String username;
    private String password;
    private String email;
}
