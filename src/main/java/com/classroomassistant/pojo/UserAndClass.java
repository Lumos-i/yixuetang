package com.classroomassistant.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DELL
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_and_class")
public class UserAndClass {
    private Integer classId;
    private Integer userId;
}
