package com.classroomassistant.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zrq
 * @ClassName UserInfoVo
 * @date 2023/1/12 14:49
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVo {
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("专业")
    private String major;
    @ApiModelProperty("班级")
    private Integer gradeId;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("籍贯")
    private String nativePlace;
}
