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
 * @ClassName User
 * @date 2023/1/10 21:00
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User {
    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty("学生id")
    private Integer studentId;
    @TableField("username")
    @ApiModelProperty("账号")
    private String userName;
    @TableField("password")
    @ApiModelProperty("密码")
    private String password;
    @TableField("email")
    @ApiModelProperty("邮箱")
    private String email;
    @TableField("photo")
    @ApiModelProperty("头像")
    private String photo;
    @TableField("sex")
    @ApiModelProperty("性别")
    private String sex;
    @TableField("name")
    @ApiModelProperty("昵称")
    private String name;
    @TableField("grade_id")
    @ApiModelProperty("班级")
    private Integer gradeId;
    @TableField("native_place")
    @ApiModelProperty("籍贯")
    private String nativePlace;
    @TableField("identity")
    @ApiModelProperty("身份")
    private Integer identity;
    @TableField(value="create_time",fill = FieldFill.INSERT)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    @TableField(value="update_time",fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;
    @TableField("is_deleted")
    @ApiModelProperty("是否删除")
    private Integer isDeleted;
}
