package com.classroomassistant.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author zrq
 * @ClassName Course
 * @date 2023/1/13 11:13
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("course")
public class Course {
    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty("id")
    private Integer id;
    @TableField("course_name")
    @ApiModelProperty("课程名")
    private String courseName;
    @TableField("creator_id")
    @ApiModelProperty("创建人id")
    private Integer creatorId;
    @TableField("details")
    @ApiModelProperty("详情")
    private String details;
    @TableField("cover")
    @ApiModelProperty("封面")
    private String cover;
    @TableField(value="create_time",fill = FieldFill.INSERT)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    @TableField(value="update_time",fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;
    @TableField("is_deleted")
    @ApiModelProperty("是否已删除")
    private Integer isDeleted;
    @TableField("course_code")
    @ApiModelProperty("是否已删除")
    private String courseCode;

}
