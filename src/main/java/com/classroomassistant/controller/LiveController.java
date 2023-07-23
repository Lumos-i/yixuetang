package com.classroomassistant.controller;

import com.classroomassistant.limit.AccessLimit;
import com.classroomassistant.service.UserService;
import com.classroomassistant.util.JsonResult;
import com.classroomassistant.util.TencentYunVedioUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.classroomassistant.util.TencentYunVedioUtil.*;

/**
 * @author zrq
 * @ClassName LiveController
 * @date 2023/1/27 16:40
 * @Description TODO
 */
@RestController
@RequestMapping("/live/")
@Slf4j
public class LiveController {

    @Autowired
    private UserService userService;

    /**
     * 生成推流全路径地址
     *      * @param pushUrl 推流地址 头部
     *      * @param stream_id 直播码  调用该方法genLiveCode() 生成
     *      * @param push_key 推流鉴权
     *      * @return
     *
     */
    @PostMapping("createPushUrl")
    @AccessLimit(seconds = 100, maxCount = 1)
    public JsonResult createPushUrl() {
        //过期时间 16进制的unix时间戳
        String txTime = to16Hex(addOneDay());
        // 填写自己的streamName
        String bizid = bizid();
        String streamName=bizid;
        StringBuffer sb = new StringBuffer();
        String pushUrlHead= TencentYunVedioUtil.pushUrl;
        sb.append(pushUrlHead+streamName).append("?")
                .append(genSign(push_key,streamName,txTime)).append("&").append("txTime=")
                .append(txTime);
        Map<String,String> map = new HashMap<>();
        map.put("address",sb.toString());
        map.put("bizid",streamName);
        return JsonResult.ok(map);
    }


    /**
     *      * 生成拉流全路径地址
     *      * @param playUrlHead  拉流的头地址
     *      * @param stream_id  直播码  调用该方法genLiveCode() 生成
     *      * @param play_key  拉流鉴权
     *      * @param end 如 .flv结尾
     *      * @return
     */
    @PostMapping("createPlayUrl")
    @ApiOperation("播放地址")
    public String createPlayUrl(String bizid) {
        //过期时间 16进制的unix时间戳

        String txTime = to16Hex(addOneDay());
        StringBuffer sb = new StringBuffer();
        String streamName=bizid;
        //String stream_id = TencentYunVedioUtil.bizid();
        sb.append(playUrl_flv+streamName+".flv").append("?").append(pushSign(play_key,streamName, txTime))
                .append("&")
                .append("txTime=")
                .append(txTime);
        return sb.toString();
    }

    @PostMapping("sendMessage")
    @ApiOperation("发送弹幕")
    public JsonResult sendMessage(String bizid,String message) {
        return userService.sendMessage(bizid, message);
    }
}