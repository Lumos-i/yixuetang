package com.classroomassistant.controller;


import com.classroomassistant.pojo.Course;
import com.classroomassistant.service.CourserService;
import com.classroomassistant.util.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @author zrq
 * @ClassName CourseController
 * @date 2023/1/13 11:26
 * @Description TODO
 */
@RestController
@RequestMapping("/course")
@Api(tags = {("课程接口")})
public class CourseController {

    @Autowired
    private CourserService courseService;

    @PostMapping("/addCourse")
    @ApiOperation("添加课程")
    public JsonResult addCourse(Course course, MultipartFile cover0){
        return courseService.addCourse(course, cover0);
    }

    @PutMapping("/updateCoverOrDetail")
    @ApiOperation("更新封面")
    public JsonResult updateCover(Integer id,String detail, MultipartFile newCover) {
        return courseService.updateCover(id,detail,newCover);
    }

    @DeleteMapping("/deleteCourse")
    @ApiOperation("删除课程")
    public JsonResult deleteCourse(Integer id) {
        return courseService.deleteCourse(id);
    }

    @GetMapping("/getOneCourse")
    @ApiOperation("得到一个课程的详细信息")
    public JsonResult getOneCourse(Integer id) {
        return courseService.getOneCourse(id);
    }

    @GetMapping("/sign")
    @ApiOperation("课程签到接口")
    public JsonResult sign(Integer id, Date createTime, Date endTime) {
        return courseService.sign(id, createTime, endTime);
    }
    @GetMapping("/getFiles")
    @ApiOperation("取出课程下所有文件")
    public JsonResult getFiles(Integer courseId,@RequestParam(value = "nodePage", defaultValue = "1") Integer nodePage,
                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return courseService.getFiles(courseId,nodePage, pageSize);
    }

    @GetMapping("/getSignDetailInfo")
    public JsonResult getSignDetailInfo(Integer id) {
        return courseService.getSignDetailInfo(id);
    }
    @GetMapping("/getCourseInfo")
    @ApiOperation("得到课程信息")
    public JsonResult getCourseInfo(Integer id) {
        return courseService.getCourseInfo(id);
    }

    @GetMapping("/getSignRecords")
    @ApiOperation("课程签到记录")
    public JsonResult getSignRecords(Integer courseId,@RequestParam(value = "nodePage", defaultValue = "1") Integer nodePage,
                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return courseService.getSignRecords(courseId,nodePage,pageSize);
    }

    @DeleteMapping("/deleteRecords")
    @ApiOperation("删除签到记录")
    public JsonResult deleteRecords(@RequestParam("ids")List<Integer> ids)  {
        return courseService.deleteRecords(ids);
    }
}
