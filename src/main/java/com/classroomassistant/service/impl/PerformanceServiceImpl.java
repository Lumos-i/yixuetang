package com.classroomassistant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.classroomassistant.mapper.PerformanceMapper;
import com.classroomassistant.pojo.Performance;
import com.classroomassistant.service.PerformanceService;
import com.classroomassistant.util.JsonResult;
import com.classroomassistant.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author zrq
 * @ClassName PerformanceServiceImpl
 * @date 2023/1/29 14:51
 * @Description TODO
 */
@Service
@Slf4j
public class PerformanceServiceImpl implements PerformanceService {
    @Autowired
    private PerformanceMapper performanceMapper;


    @Override
    public JsonResult getMyPerformance(Integer termId) {
        if (termId == null) {
            return JsonResult.errorException("请输入选择时间");
        }
        Integer userId = UserUtils.getUserId();
        LambdaQueryWrapper<Performance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Performance::getUserId,userId).
                eq(Performance::getTermId,termId);
        List<Performance> performances = performanceMapper.selectList(wrapper);
        return performances.isEmpty() ? JsonResult.errorException("无数据") : JsonResult.ok(performances);
    }

    @Override
    public JsonResult getMyPerformanceByUserId(Integer userId, Integer courseId, Integer termId) {
       if (userId== null || courseId == null || termId == null) {
           return JsonResult.errorException("数据不能为空");
       }
        LambdaQueryWrapper<Performance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Performance::getUserId,userId)
                .eq(Performance::getCourseId,courseId)
                .eq(Performance::getTermId,termId);
        List<Performance> performances = performanceMapper.selectList(wrapper);
        return performances.isEmpty() ? JsonResult.errorException("无数据") : JsonResult.ok(performances);
    }
}
