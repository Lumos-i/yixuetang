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
 * @ClassName Sign
 * @date 2023/1/16 19:48
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sign")
public class Sign {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("id")
    private Integer id;
    @TableField("course_id")
    @ApiModelProperty("课程id")
    private Integer courseId;
    @TableField(value="create_time",fill = FieldFill.INSERT)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    @TableField("end_time")
    private Date endTime;

    @TableField("signed_nums")
    private Integer signedNums;
    @TableField("no_signed_nums")
    private Integer noSignedNums;

    @TableField("unsigned_ids")
    private String unsignedIds;
    @TableField("signed_ids")
    private String signedIds;

}
