package com.classroomassistant.service;

import com.classroomassistant.util.JsonResult;

import java.math.BigDecimal;

/**
 * @author zrq
 * @ClassName TeacherService
 * @date 2023/1/14 15:03
 * @Description TODO
 */
public interface TeacherService {
    JsonResult myCourse(Integer nodePage, Integer pageSize);

    JsonResult deleteStudentFromCourse(Integer courseId, Integer studentId);

    JsonResult getCourseSignInfo(Integer signId, Integer gradeId);

    JsonResult randomName(Integer id);

    JsonResult grouping(Integer courseId, Integer studentNums, Integer sex, Integer performance);

    JsonResult announcement(Integer courseId, String content);

    JsonResult addPerformance(Integer userId, Integer courseId, BigDecimal performance, Integer termId);

    JsonResult groupingAnd(Boolean sex, Boolean performance, Integer courseId, Integer studentNums);
}
