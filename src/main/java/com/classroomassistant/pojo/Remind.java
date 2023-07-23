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
 * @ClassName Remind
 * @date 2023/2/1 11:34
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("remind")
public class Remind {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("id")
    private Integer id;
    @TableField("user_id")
    @ApiModelProperty("用户id")
    private Integer userId;
    @TableField("course_id")
    @ApiModelProperty("课程id")
    private Integer courseId;
    @TableField("content")
    @ApiModelProperty("内容")
    private String content;
    @TableField("type")
    @ApiModelProperty("类型")
    private Integer type;
    @TableField(value="create_time",fill = FieldFill.INSERT)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    @TableField("status")
    @ApiModelProperty("状态")
    private Integer status;
}
