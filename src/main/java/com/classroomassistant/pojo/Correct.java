package com.classroomassistant.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.classroomassistant.vo.Description;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author DELL
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "correct",autoResultMap = true)
public class Correct {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer hId;
    private Integer userId;
    private BigDecimal sScore;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Description> deScore;
    private BigDecimal allScore;
    private Integer questionCount;
    private Integer questionCorrect;
}
