package com.classroomassistant.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.classroomassistant.mapper.CourseAndUserMapper;
import com.classroomassistant.pojo.CourseAndUser;
import com.classroomassistant.pojo.User;
import com.classroomassistant.service.CourserService;
import com.classroomassistant.service.TeacherService;
import com.classroomassistant.service.UserService;
import com.classroomassistant.util.JsonResult;
import com.classroomassistant.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zrq
 * @ClassName TeacherController
 * @date 2023/1/14 13:17
 * @Description TODO
 */
@RestController
@RequestMapping("/teacher")
@Api(tags = {("教师接口")})
@PreAuthorize("hasAnyAuthority('1,2')")
public class TeacherController {
    @Autowired
    private CourserService courseService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private UserService userService;
    @Autowired
    private CourseAndUserMapper courseAndUserMapper;

    @PostMapping("/enterCourseByClass")
    @ApiOperation("加入课程")
    public JsonResult enterCourseByClass(Integer courseId, List<Integer> classId) {
        return courseService.enterCourseByClass(courseId,classId);
    }

    @GetMapping("/myCourse")
    @ApiOperation("该用户创建的所有课程")
    public JsonResult myCourse (@RequestParam(value = "nodePage", defaultValue = "1") Integer nodePage,
                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return teacherService.myCourse(nodePage, pageSize);
    }

    @GetMapping("/courseStudents")
    @ApiOperation("课程中的所有学生")
    public JsonResult courseStudents(Integer classId, @RequestParam(value = "nodePage", defaultValue = "1") Integer nodePage,
                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return userService.courseStudents(classId, nodePage, pageSize);
    }

    @DeleteMapping("/deleteStudentFromCourse")
    @ApiOperation("将学生从课程中删除")
    public JsonResult deleteStudentFromCourse(Integer courseId,Integer studentId) {
        return teacherService.deleteStudentFromCourse(courseId, studentId);
    }

    @GetMapping("/getCourseSignInfo")
    @ApiOperation("签到信息")
    public JsonResult getCourseSignInfo(Integer signId, Integer gradeId) {
        return teacherService.getCourseSignInfo(signId, gradeId);
    }

    @GetMapping("/randomName")
    @ApiOperation("随机点名")
    public JsonResult randomName(Integer id) {
        return teacherService.randomName(id);
    }

    @GetMapping("/grouping")
    @ApiOperation("分组")
    public JsonResult grouping(Integer courseId, Integer studentNums, Integer sex, Integer performance) {
        return teacherService.grouping(courseId, studentNums, sex, performance);
    }

    @PostMapping("/uploadFile")
    @ApiOperation("上传文件")
    public JsonResult uploadFile(Integer courseId, String fileName, MultipartFile fileAddress, Date createTime) {
        return userService.uploadFile(courseId, fileName, fileAddress, createTime);
    }

    @PutMapping("/updateUserInfo")
    @ApiOperation("更新用户信息")
    public JsonResult updateUserInfoById(UserInfoVo userInfoVo, Integer id) {
        return userService.updateUserInfoById(userInfoVo, id);
    }

    @PostMapping("/announcement")
    @ApiOperation("发布通知")
    public JsonResult announcement(Integer courseId, String content) {
        return teacherService.announcement(courseId, content);
    }

    @PostMapping("/addPerformance")
    @ApiOperation("提交成绩")
    public JsonResult addPerformance(Integer userId, Integer courseId, BigDecimal performance,Integer termId) {
        return teacherService.addPerformance(userId,courseId,performance, termId);
    }

    @GetMapping("/groupingAnd")
    @ApiOperation("分组(副本)")
    public JsonResult groupingAnd(Boolean sex,Boolean performance,Integer courseId,Integer studentNums) {
        return teacherService.groupingAnd(sex,performance,courseId,studentNums);
    }
    @DeleteMapping("/deleteFile")
    @ApiOperation("删除附件")
    public JsonResult deleteFile(Integer id) {
        return userService.deleteFile(id);
    }
}


