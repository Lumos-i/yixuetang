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
 * @ClassName UploadFile
 * @date 2023/1/30 14:57
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("file_table")
public class UploadFile {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField("user_id")
    @ApiModelProperty("用户id")
    private Integer userId;
    @TableField("course_id")
    @ApiModelProperty("课程id")
    private Integer courseId;
    @TableField("file_name")
    @ApiModelProperty("文件名")
    private String fileName;
    @TableField("file_address")
    @ApiModelProperty("文件地址")
    private String fileAddress;
    @TableField(value="create_time",fill = FieldFill.INSERT)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
}
