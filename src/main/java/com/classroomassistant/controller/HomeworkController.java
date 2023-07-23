package com.classroomassistant.controller;

import com.classroomassistant.pojo.Correct;
import com.classroomassistant.pojo.Homework;
import com.classroomassistant.pojo.Task;
import com.classroomassistant.pojo.UserAnswer;
import com.classroomassistant.service.HomeworkService;
import com.classroomassistant.util.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

/**
 * @author DELL
 **/
@RestController
@RequestMapping("/homework")
@Api(tags = {("作业接口")})
public class HomeworkController {
    @Autowired
    private HomeworkService homeworkService;
    @PostMapping("createHomework")
    @ApiOperation("创建作业")
    public JsonResult createHomework(@RequestBody Homework homework){
        return homeworkService.createHomework(homework);
    }

    @PutMapping("/updateHomework")
    @ApiOperation("修改作业")
    public JsonResult updateHomework(@RequestBody Homework homework){
        return homeworkService.updateHomework(homework);
    }

    @DeleteMapping("deleteHomework")
    @ApiOperation("删除作业")
    public JsonResult deleteHomework(Integer homeworkId){
        return homeworkService.deleteHomework(homeworkId);
    }

    @GetMapping("getHomework")
    @ApiOperation("获取作业")
    public JsonResult getHomework(Integer beginIndex,Integer size,Integer courseId){
        return homeworkService.getHomework(beginIndex,size,courseId);
    }

    @PostMapping("submitAnswer")
    @ApiOperation("提交作业答案")
    public JsonResult submitAnswer(@RequestBody UserAnswer userAnswer){
        return homeworkService.submitAnswer(userAnswer);
    }

    @PostMapping("correctHomework")
    @ApiOperation("批改作业")
    public JsonResult correctHomework(@RequestBody Correct correct){
        return homeworkService.correctHomework(correct);
    }

    @GetMapping("getOutcome")
    @ApiOperation("获取关于班级成绩分析")
    public JsonResult getOutcome(){
        return homeworkService.getOutcome();
    }

    @GetMapping("/getHomeworkById")
    @ApiOperation("获取单个作业")
    public JsonResult getHomeworkById(Integer homeworkId,Integer studentId){
        return homeworkService.getHomeworkById(homeworkId,studentId);
    }

    @PostMapping("publishTask")
    @ApiOperation("发布任务")
    public JsonResult publishTask(Task task){
        return homeworkService.publishTask(task);
    }

    @DeleteMapping("deleteTask")
    @ApiOperation("撤销任务")
    public JsonResult deleteTask(Integer taskId){
        return homeworkService.deleteTask(taskId);
    }

    @GetMapping("getTask")
    @ApiOperation("获取任务")
    public JsonResult getTask(Integer courseId,Integer beginIndex,Integer size){
        return homeworkService.getTask(courseId,beginIndex,size);
    }

    @GetMapping("getTaskInfo")
    @ApiOperation("获取任务详情")
    public JsonResult getTaskInfoById(Integer taskId,Integer gradeId,String sex,Integer status,Integer beginIndex,Integer size){
        return homeworkService.getTaskInfoById(taskId, gradeId, sex, status, beginIndex, size);
    }

    @PostMapping("publishQuestion")
    @ApiOperation("发布课堂问题")
    public JsonResult publishQuestion(String union,@RequestBody String question,Integer time,String answer){
        return homeworkService.publishQuestion(union,question,time,answer);
    }

    @PostMapping("publishAnswer")
    @ApiOperation("回复课堂问题")
    public JsonResult publishAnswer(String union,String answer){
        return homeworkService.publishAnswer(union,answer);
    }

    @GetMapping("getCourseOutCome")
    @ApiOperation("获取课程回答问题的结果分析")
    public JsonResult getCourseOutCome(String union){
        return homeworkService.getCourseOutCome(union);
    }

    @GetMapping("getCourseQuestion")
    @ApiOperation("获取课堂问题")
    public JsonResult getCourseQuestion(String union){
        return homeworkService.getCourseQuestion(union);
    }

    @PostMapping("/addPicture")
    @ApiOperation("上传图片")
    public JsonResult addPicture(MultipartFile file){
        return homeworkService.addPicture(file);
    }

    @PostMapping("/deletePicture")
    @ApiOperation("删除图片")
    public JsonResult deletePicture(@RequestParam String fileName){
        return homeworkService.deletePicture(fileName);
    }


}
