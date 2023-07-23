package com.classroomassistant.controller;

import com.classroomassistant.service.CourserService;
import com.classroomassistant.service.UserService;
import com.classroomassistant.util.JsonResult;
import com.classroomassistant.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author zrq
 * @ClassName AdminController
 * @date 2023/1/10 21:13
 * @Description TODO
 */
@RestController
@RequestMapping("/admin")
@Api(tags = {("管理员接口")})
@PreAuthorize("hasAnyAuthority('2')")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private CourserService courseService;
    @GetMapping("/getAllUser")
    @ApiOperation("取出所有用用户 ")
    public JsonResult getAllUser(@RequestParam(value = "nodePage", defaultValue = "1") Integer nodePage,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                 Integer gradeId,String sex ) {
        return userService.getAllUser(nodePage, pageSize,gradeId,sex);
    }

    @GetMapping("/getAllCourse")
    @ApiOperation("取出所有课程")
    public JsonResult getAllCourse(@RequestParam(value = "nodePage", defaultValue = "1") Integer nodePage,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                   Integer userId) {
        return courseService.getAllCourse(nodePage, pageSize, userId);
    }

    @DeleteMapping("/deleteUser")
    @ApiOperation("删除用户")
    public JsonResult deleteUser(Integer id) {
        return userService.deleteUser1(id);
    }

    @PutMapping("/updateUserInfo")
    @ApiOperation("更新用户信息")
    public JsonResult updateUserInfoById(UserInfoVo userInfoVo, Integer id) {
        return userService.updateUserInfoById(userInfoVo, id);
    }

    @PostMapping("/resetPassword")
    @ApiOperation("重置密码")
    public JsonResult resetPassword(Integer id) {
        return userService.resetPassword(id);
    }

    @PostMapping("/addTerm")
    @ApiOperation("学年添加")
    public JsonResult addTerm(Date beginTime, Date endTime, String name) {
        return userService.addTerm(beginTime, endTime, name);
    }
    @PutMapping("/updatePower")
    @ApiOperation("修改权限")
    public JsonResult updatePower(Integer id, Integer power) {
        return userService.updatePower(id, power);
    }
}
