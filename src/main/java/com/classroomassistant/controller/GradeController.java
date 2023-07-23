package com.classroomassistant.controller;

import com.classroomassistant.pojo.Grade;
import com.classroomassistant.service.GradeService;
import com.classroomassistant.util.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author DELL
 **/
@RestController
@RequestMapping("/grade")
@Api(tags = {("班级接口")})
public class GradeController {
    @Autowired
    private GradeService gradeService;
    @PostMapping("/createGrade")
    @ApiOperation("创建班级")
    public JsonResult createGrade(String className){
        return gradeService.createGrade(className);
    }

    @DeleteMapping("/deleteGrade")
    @ApiOperation("删除班级")
    public JsonResult deleteGrade(Integer gradeId){
        return gradeService.deleteGrade(gradeId);
    }

    @PutMapping("/updateGrade")
    @ApiOperation("修改班级信息")
    public JsonResult updateGrade(Grade grade){
        return gradeService.updateGrade(grade);
    }

    @GetMapping("getGrade")
    @ApiOperation("获取班级信息")
    public JsonResult getGrade(Integer beginIndex,Integer size){
        return gradeService.getGrade(beginIndex,size);
    }

    @PostMapping("/enterGrade")
    @ApiOperation("加入班级")
    public JsonResult enterGrade(String regexCode){
        return gradeService.enterGrade(regexCode);
    }

    @GetMapping("getGradeByCourse")
    @ApiOperation("通过课程查班级信息")
    public JsonResult getGradeByCourse(Integer courseId){
        return gradeService.getGradeByCourse(courseId);
    }

    @GetMapping("getAllGrade")
    @ApiOperation("获取所有班级")
    public JsonResult getAllGrade(){
        return gradeService.getAllGrade();
    }
}
