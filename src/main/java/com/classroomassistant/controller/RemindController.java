package com.classroomassistant.controller;

import com.classroomassistant.service.RemindService;
import com.classroomassistant.util.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.util.JAXBSource;

/**
 * @author zrq
 * @ClassName RemindController
 * @date 2023/2/1 11:45
 * @Description TODO
 */
@RestController
@RequestMapping("/message")
@Api(tags = {("信息接口")})
public class RemindController {
    @Autowired
    private RemindService remindService;

    @PostMapping("/addMessage")
    @ApiOperation("添加信息")
    public JsonResult addMessage(Integer courseId,String content, Integer type) {
        return remindService.addMessage(courseId,content,type);
    }

    @GetMapping("/getMessage")
    @ApiOperation("得到信息")
    public JsonResult getMessage(Integer courseId,Integer type,@RequestParam(value = "nodePage", defaultValue = "1") Integer nodePage,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return remindService.getMessage(courseId,type,nodePage,pageSize);
    }
}
