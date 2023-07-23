package com.classroomassistant.service;

import com.classroomassistant.pojo.Grade;
import com.classroomassistant.util.JsonResult;

public interface GradeService {
    JsonResult createGrade(String className);

    JsonResult deleteGrade(Integer gradeId);

    JsonResult updateGrade(Grade grade);

    JsonResult getGrade(Integer beginIndex, Integer size);

    JsonResult enterGrade(String regexCode);

    JsonResult getGradeByCourse(Integer courseId);

    JsonResult getAllGrade();
}
