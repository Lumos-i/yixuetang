package com.classroomassistant.dto;

import com.classroomassistant.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zrq
 * @ClassName UserInfoDto
 * @date 2023/2/5 15:05
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private User user;
    private String className;
}
