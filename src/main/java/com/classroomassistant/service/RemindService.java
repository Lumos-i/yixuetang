package com.classroomassistant.service;

import com.classroomassistant.util.JsonResult;

/**
 * @author zrq
 * @ClassName RemindService
 * @date 2023/2/1 11:48
 * @Description TODO
 */
public interface RemindService {
    JsonResult addMessage(Integer courseId, String content, Integer type);

    JsonResult getMessage(Integer courseId,Integer type, Integer nodePage, Integer pageSize);

}
