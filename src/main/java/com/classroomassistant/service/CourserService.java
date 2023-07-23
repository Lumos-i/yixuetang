package com.classroomassistant.service;

import com.classroomassistant.pojo.Course;
import com.classroomassistant.util.JsonResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @author zrq
 * @ClassName CorseService
 * @date 2023/1/13 14:22
 * @Description TODO
 */
public interface CourserService {
    JsonResult addCourse(Course course, MultipartFile cover0);

    JsonResult updateCover(Integer id, String detail, MultipartFile newCover);

    JsonResult deleteCourse(Integer id);

    JsonResult enterCourseByClass(Integer courseId, List<Integer> classId);

    JsonResult getAllCourse(Integer nodePage, Integer pageSize, Integer userId);

    JsonResult getEnteredCourse(Integer nodePage, Integer pageSize);

    JsonResult getOneCourse(Integer id);

    JsonResult sign(Integer id, Date createTime, Date endTime);

    JsonResult getFiles(Integer courseId, Integer nodePage, Integer pageSize);

    JsonResult getSignDetailInfo(Integer id);

    JsonResult getCourseInfo(Integer id);

    JsonResult getSignRecords(Integer courseId, Integer nodePage, Integer pageSize);

    JsonResult deleteRecords(List<Integer> ids);
}
