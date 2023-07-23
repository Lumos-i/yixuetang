package com.classroomassistant.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zrq
 * @ClassName Proformance
 * @date 2023/1/29 13:40
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("performance")
public class Performance {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("id")
    private Integer id;
    @TableField("course_id")
    @ApiModelProperty("课程id")
    private Integer courseId;
    @TableField("user_id")
    @ApiModelProperty("用户id")
    private Integer userId;
    @TableField("performance")
    @ApiModelProperty("得分")
    private BigDecimal performance;
    @TableField(value="create_time",fill = FieldFill.INSERT)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    @TableField("is_deleted")
    @ApiModelProperty("删除")
    private Integer isDeleted;
    @TableField("term_id")
    @ApiModelProperty("学期id")
    private Integer termId;

}
