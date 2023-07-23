package com.classroomassistant.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author DELL
 **/
@AllArgsConstructor
@Data
@NoArgsConstructor
@TableName("grade")
@ApiModel
public class Grade {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("class_name")
    @ApiModelProperty("班级名")
    private String className;
    @TableField("create_name")
    @ApiModelProperty("创建者id")
    private Integer createName;
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    @ApiModelProperty("班级码")
    @TableField("code")
    private String code;
    @ApiModelProperty("是否删除")
    @TableField("deleted")
    private Boolean deleted;

    public Grade(Integer id, Boolean deleted) {
        this.id = id;
        this.deleted = deleted;
    }
}
