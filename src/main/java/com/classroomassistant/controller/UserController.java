package com.classroomassistant.controller;

import com.classroomassistant.limit.AccessLimit;
import com.classroomassistant.service.CourserService;
import com.classroomassistant.service.PerformanceService;
import com.classroomassistant.service.UserService;
import com.classroomassistant.util.JsonResult;
import com.classroomassistant.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author zrq
 * @ClassName UserController
 * @date 2023/1/11 17:55
 * @Description TODO
 */
@RestController
@RequestMapping("/user")
@Api(tags = {("用户接口")})
public class UserController {
    @Resource
    private UserService userService;
    @Autowired
    private CourserService courseService;
    @Autowired
    private PerformanceService performanceService;
    @PostMapping("/outLogin/register")
    @ApiOperation("注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "账号"),
            @ApiImplicitParam(name = "password", value = "密码"),
            @ApiImplicitParam(name = "email", value = "邮箱"),
            @ApiImplicitParam(name = "code", value = "验证码"),
    })
    @AccessLimit(seconds = 10, maxCount = 5)
    public JsonResult register(String username, String password, String email, String code) {
        return userService.register(username, password, email, code);
    }

    @PostMapping("/logged/sendCode")
    @ApiOperation("发送验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "邮箱")
    })
    @AccessLimit(seconds = 10, maxCount = 5)
    public JsonResult sendCode(String email) {
        return userService.sendCode(email);
    }

    @PostMapping("/outLogin/login")
    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名或邮箱"),
            @ApiImplicitParam(name = "password", value = "密码")
    })
    public JsonResult login(String username, String password) {
        return userService.login(username, password);
    }

    @GetMapping("/logged/logOut")
    @ApiOperation(("退出登录"))
    @AccessLimit(seconds = 10, maxCount = 5)
    public JsonResult logOut() {
        return userService.logOut();
    }

    @PutMapping("/updatePhoto")
    @ApiOperation("更新头像")
    public JsonResult updatePhoto(MultipartFile photo) {
        return userService.updatePhoto(photo);
    }

    @PutMapping("/updateUserInfo")
    @ApiOperation("更新用户信息")
    public JsonResult updateUserInfo(UserInfoVo userInfoVo) {
        return userService.updateUserInfo(userInfoVo);
    }
    @PutMapping("/outLogin/forgetPassword")
    @ApiOperation("忘记密码")
    public JsonResult forgetPassword(String email, String code, String newPassword) {
        return userService.forgetPassword(email,code,newPassword);
    }
    @DeleteMapping("/deleteUser")
    @ApiOperation("注销账户")
    public JsonResult deleteUser(){
        return userService.deleteUser();
    }

    @PutMapping("/enterCourseByCode")
    @ApiOperation("加入课程")
    public JsonResult enterCourseByCode(String code){
        return userService.enterCourseByCode(code);
    }


    @GetMapping("/getEnteredCourse")
    @ApiOperation("获取加入的课程")
    public JsonResult getEnteredCourse(@RequestParam(value = "nodePage", defaultValue = "1") Integer nodePage,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return courseService.getEnteredCourse(nodePage, pageSize);
    }
    @GetMapping("/getUserInfo")
    @ApiOperation("得到用户信息")
    public JsonResult getUserInfo() {
        return userService.getUserInfo();
    }

    @PostMapping("/sign")
    @ApiOperation("签到")
    public JsonResult sign(Integer signId,Integer courseId) {
        return userService.sign(signId, courseId);
    }
    @PutMapping("/updatePassword")
    @ApiOperation("修改密码")
    public JsonResult updatePassword(String email, String code, String newPassword) {
        return userService.updatePassword(email,code,newPassword);
    }

    @GetMapping("/getMyPerformance")
    @ApiOperation("课程成绩")
    public JsonResult getMyPerformance(Integer termId) {
        return performanceService.getMyPerformance(termId);
    }
    @GetMapping("/getMyPerformanceByUserId")
    @ApiOperation("课程成绩")
    public JsonResult getMyPerformanceByUserId(Integer userId, Integer courseId, Integer termId) {
        return performanceService.getMyPerformanceByUserId(userId, courseId, termId);
    }
    @PostMapping("/uploadFile")
    @ApiOperation("上传文件")
    public JsonResult uploadFile(Integer courseId, String fileName, MultipartFile fileAddress, Date createTime) {
        return userService.uploadFile(courseId, fileName, fileAddress, createTime);
    }

    @GetMapping("/allAddress")
    @ApiOperation("籍贯")
    public JsonResult allAddress() {
        return userService.allAddress();
    }

    @GetMapping("/randomName")
    @ApiOperation("取出随机点名结果")
    public JsonResult getRandomName(Integer courseId) {
        return userService.getRandomName(courseId);
    }

    @GetMapping("/getTeacherInfo")
    @ApiOperation("获得教师信息")
    public JsonResult getTeacherInfo(Integer id) {
        return userService.getTeacherInfo(id);
    }

    @GetMapping("/getTerm")
    @ApiOperation("学期")
    public JsonResult getTerm() {
        return userService.getTerm();
    }

    @GetMapping("/whetherSign")
    @ApiOperation("是否签到")
    public JsonResult whetherSign(Integer courseId) {
        return userService.whetherSign(courseId);
    }

    @GetMapping("/myGrouping")
    @ApiOperation("个人分组")
    public JsonResult myGrouping(Integer courseId) {
        return userService.myGrouping(courseId);
    }

    @GetMapping("/getGroupById")
    @ApiOperation("得到分组")
    public JsonResult getGroupById(Integer courseId){
        return userService.getGroupById(courseId);
    }

    @GetMapping("/courseStudents")
    @ApiOperation("课程中的所有学生")
    public JsonResult courseStudents(Integer classId, @RequestParam(value = "nodePage", defaultValue = "1") Integer nodePage,
                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return userService.courseStudents(classId, nodePage, pageSize);
    }
   @GetMapping("/getCourseSign")
    @ApiOperation("得到课程签到")
    public JsonResult getCourseSign(Integer courseId,@RequestParam(value = "nodePage", defaultValue = "1") Integer nodePage,
                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return userService.getCourseSign(courseId,nodePage, pageSize);
   }
}
