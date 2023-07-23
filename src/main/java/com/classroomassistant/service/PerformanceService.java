package com.classroomassistant.service;

import com.classroomassistant.util.JsonResult;

import java.util.Date;

/**
 * @author zrq
 * @ClassName PerformanceService
 * @date 2023/1/29 14:50
 * @Description TODO
 */
public interface PerformanceService {
    JsonResult getMyPerformance(Integer termId);

    JsonResult getMyPerformanceByUserId(Integer userId, Integer courseId, Integer termId);
}
