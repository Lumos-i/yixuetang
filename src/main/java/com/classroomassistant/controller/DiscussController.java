package com.classroomassistant.controller;

import com.classroomassistant.pojo.Discuss;
import com.classroomassistant.pojo.Topic;
import com.classroomassistant.service.DiscussService;
import com.classroomassistant.util.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/discuss")
@Api(tags = {("讨论接口")})
public class DiscussController {
    @Autowired
    private DiscussService discussService;
    @PostMapping("/publishTopic")
    @ApiOperation("发布论题")
    public JsonResult publishTopic(Topic topic){
        return discussService.publishTopic(topic);
    }

    @GetMapping("/getTopic")
    @ApiOperation("获取课程论题")
    public JsonResult getTopic(Integer courseId,Integer beginIndex,Integer size){
        return discussService.getTopic(courseId, beginIndex, size);
    }

    @DeleteMapping("deleteTopic")
    @ApiOperation("撤回论题")
    public JsonResult deleteTopic(Integer topicId){
        return discussService.deleteTopic(topicId);
    }

    @PostMapping("publishDiscuss")
    @ApiOperation("发布对论题的评论")
    public JsonResult publishDiscuss(Discuss discuss){
        return discussService.publishDiscuss(discuss);
    }

    @GetMapping("getAllDiscuss")
    @ApiOperation("获取所有论题")
    public JsonResult getAllDiscuss(Integer topicId,Integer beginIndex,Integer size){
        return discussService.getAllDiscuss(topicId,beginIndex,size);
    }

    @DeleteMapping("deleteDiscuss")
    @ApiOperation("删除评论")
    public JsonResult deleteDiscuss(Integer discussId){
        return discussService.deleteDiscuss(discussId);
    }
}
