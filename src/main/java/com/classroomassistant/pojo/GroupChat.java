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
 * @ClassName GroupChat
 * @date 2023/2/1 16:17
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("group_chat")
public class GroupChat {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("user_id")
    @ApiModelProperty("用户id")
    private Integer userId;
    @TableField("course_id")
    @ApiModelProperty("课程id")
    private Integer courseId;
    @TableField("group_id")
    @ApiModelProperty("小组id")
    private String groupId;
    @TableField("content")
    @ApiModelProperty("内容")
    private String content;
    @TableField(value="create_time",fill = FieldFill.INSERT)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
 }
