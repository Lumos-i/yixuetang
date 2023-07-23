package com.classroomassistant.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author DELL
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String taskName;
    private Integer belongCourseId;
    @TableField(value="create_time",fill = FieldFill.INSERT)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;
    private Integer homeworkId;
    private Boolean deleted;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty("开始时间")
    private Date beginTime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty("结束时间")
    private Date endTime;
    private Integer type;
    private Integer createUserId;

    public Task(Integer id, Boolean deleted) {
        this.id = id;
        this.deleted = deleted;
    }
}
