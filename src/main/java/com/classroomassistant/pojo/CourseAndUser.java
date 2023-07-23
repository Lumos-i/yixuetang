package com.classroomassistant.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zrq
 * @ClassName CourseAndUser
 * @date 2023/1/14 13:34
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("course_and_user")
public class CourseAndUser {
    private Integer courseId;
    private Integer userId;
}
