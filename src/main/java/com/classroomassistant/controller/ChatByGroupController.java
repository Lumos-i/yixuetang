package com.classroomassistant.controller;

import com.classroomassistant.config.WsServer;
import com.classroomassistant.mapper.GroupChatMapper;
import com.classroomassistant.pojo.GroupChat;
import com.classroomassistant.service.UserService;
import com.classroomassistant.util.JsonResult;
import com.classroomassistant.util.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zrq
 * @ClassName ChatByGroupController
 * @date 2023/2/1 15:54
 * @Description TODO
 */
@RestController
@RequestMapping("/chatGroup")
@Api(tags = {("分组聊天接口")})
public class ChatByGroupController {
    @Autowired
    private UserService userService;
    @Autowired
    private GroupChatMapper groupChatMapper;

    @GetMapping("/groupInfo")
    @ApiOperation("课程分组信息")
    public JsonResult groupInfo(Integer courseId) {
        return userService.groupInfo(courseId);
    }

    @PostMapping("/sendMessage")
    @ApiOperation("发送信息")
    public JsonResult sendMessage(String content, String groupId, Integer courseId){
       return userService.sendMessage2(content,groupId,courseId);
    }

    @GetMapping("/getHistory")
    @ApiOperation("得到聊天记录")
    public JsonResult getMessageHistory(Integer courseId, String groupId, @RequestParam(value = "nodePage", defaultValue = "1") Integer nodePage,
                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return userService.getMessageHistory(courseId,groupId, nodePage, pageSize);
    }

}
