package com.classroomassistant.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.classroomassistant.vo.Answer;
import com.classroomassistant.vo.Question;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author DELL
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "homework",autoResultMap = true)
@ApiModel
public class Homework {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("homework_name")
    @ApiModelProperty("作业名")
    private String homeworkName;
    @TableField(value="create_time",fill = FieldFill.INSERT)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty("开始时间")
    private Date beginTime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty("结束时间")
    private Date endTime;
    @TableField(typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("问题")
    private List<Question> question;
    @TableField(typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("答案")
    private List<Answer> answer;
    @ApiModelProperty("是否删除")
    private Boolean deleted;
    @ApiModelProperty("教师id")
    private Integer teacherId;
    @ApiModelProperty("问题数量")
    private Integer questionCount;
    @ApiModelProperty("备注")
    private String remark;

    public Homework(Integer id, Boolean deleted) {
        this.id = id;
        this.deleted = deleted;
    }
}
