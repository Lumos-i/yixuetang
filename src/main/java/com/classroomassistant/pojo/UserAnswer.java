package com.classroomassistant.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.classroomassistant.vo.Answer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author DELL
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user_answer",autoResultMap = true)
public class UserAnswer {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer hId;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Answer> userAnswer;
    private Integer duration;
    private Integer status;
}
