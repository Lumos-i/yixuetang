package com.classroomassistant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.classroomassistant.mapper.*;
import com.classroomassistant.pojo.*;

import com.classroomassistant.service.CourserService;
import com.classroomassistant.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author zrq
 * @ClassName CourseServiceImpl
 * @date 2023/1/13 14:22
 * @Description TODO
 */
@Service
@Slf4j
public class CourseServiceImpl implements CourserService {


    @Autowired
    private CosUtil cosUtil;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CourseAndUserMapper courseAndUserMapper;
    @Autowired
    private SignMapper signMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private UploadFileMapper uploadFileMapper;

    @Override
    public JsonResult addCourse(Course course, MultipartFile cover0) {
        if (StringUtils.isEmpty(course.getCourseName()) || StringUtils.isEmpty(course.getDetails())) {
            return JsonResult.errorException("数据不能为空");
        }
        if (!FileTypeUtil.isImage(cover0)) {
            return JsonResult.errorException("文件类型不正确");
        }
        String upload = cosUtil.upload(cover0, 2);
        course.setCover(upload);
        Integer userId = UserUtils.getUserId();
        course.setCreatorId(userId);
        String code = JsonResult.createCode(8);
        course.setCourseCode(code);
        int insert = courseMapper.insert(course);
        return insert > 0 ? JsonResult.ok(code) : JsonResult.errorException("添加失败");
    }

    @Override
    public JsonResult updateCover(Integer id, String detail, MultipartFile newCover) {
        if (id == null) {
            return JsonResult.errorException("无数据");
        }
        if (StringUtils.isEmpty(detail)) {
            if (!FileTypeUtil.isImage(newCover)) {
                return JsonResult.errorException("文件格式错误");
            }
        }
        if (!StringUtils.isEmpty(detail) ||!newCover.isEmpty()) {
            if (!FileTypeUtil.isImage(newCover)) {
                return JsonResult.errorException("文件格式错误");
            }
        }
        String upload = cosUtil.upload(newCover, 2);
        Course course = new Course();
        course.setId(id);
        course.setCover(upload);
        course.setDetails(detail);
        int update = courseMapper.updateById(course);
        return update > 0 ? JsonResult.ok("已更新") : JsonResult.errorException("服务器错误");
    }

    @Override
    public JsonResult deleteCourse(Integer id) {
        if (id != null) {
            Course course = new Course();
            course.setId(id);
            course.setIsDeleted(1);
            int update = courseMapper.updateById(course);
            LambdaQueryWrapper<CourseAndUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CourseAndUser::getCourseId,id);
            LambdaQueryWrapper<Course> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(Course::getId,id);
            courseMapper.delete(wrapper1);
            courseAndUserMapper.delete(wrapper);
            return update > 0 ? JsonResult.ok("已删除") : JsonResult.errorException("服务器错误");
        }
        return null;
    }

    @Override
    public JsonResult enterCourseByClass(Integer courseId, List<Integer> classId) {
        if (courseId == null || classId == null) {
            return JsonResult.errorException("数据不能为空");
        }
        //选出所有的学生
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>();
        wrapper.in(User::getGradeId,classId);

        List<User> users = userMapper.selectList(wrapper);
        if (users == null || users.isEmpty()) {
            return JsonResult.errorException("无数据");
        }
        int i = 0;
        for (User user : users) {
            i = courseAndUserMapper.insert(new CourseAndUser(courseId,user.getStudentId()));
        }
        return i > 0 ? JsonResult.ok("已添加") : JsonResult.errorException("错误");
    }

    @Override
    public JsonResult getAllCourse(Integer nodePage, Integer pageSize, Integer userId) {
        if (nodePage <= 0) {
            return JsonResult.errorException("数据错误");
        }
        if(pageSize <= 0) {
            return JsonResult.errorException("数据错误");
        }
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(userId != null, Course::getCreatorId, userId);
        Page<Course> page = new Page<>(nodePage, pageSize);
        Page<Course> coursePage = courseMapper.selectPage(page, wrapper);
        return coursePage.getRecords().isEmpty() ? JsonResult.errorException("无数据") : JsonResult.ok(coursePage);
    }

    @Override
    public JsonResult getEnteredCourse(Integer nodePage, Integer pageSize) {
        if (nodePage <= 0) {
            return JsonResult.errorException("数据错误");
        }
        if(pageSize <= 0) {
            return JsonResult.errorException("数据错误");
        }
        Integer userId = UserUtils.getUserId();
        Page<CourseAndUser> page = new Page<>(nodePage, pageSize);
        LambdaQueryWrapper<CourseAndUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseAndUser::getUserId,userId);
        Page<CourseAndUser> courseAndUserPage = courseAndUserMapper.selectPage(page, wrapper);
        return courseAndUserPage.getSize() == 0 ? JsonResult.errorException("无数据") : JsonResult.ok(courseAndUserPage);
    }

    @Override
    public JsonResult getOneCourse(Integer id) {
        if(id == null) {
            return JsonResult.errorException("数据不能为空");
        }
        Course course = courseMapper.selectById(id);
        return course == null ? JsonResult.errorException("无数据") : JsonResult.ok(course);
    }

    @Override
    public JsonResult sign(Integer id, Date createTime, Date endTime) {
        if (id == null) {
            return JsonResult.errorException("数据不能为空");
        }
        redisCache.deleteObject("signed"+id);
        LambdaQueryWrapper<CourseAndUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseAndUser::getCourseId,id);
        List<CourseAndUser> courseAndUsers = courseAndUserMapper.selectList(wrapper);
        List<Integer> ids = new ArrayList<>();
        String unSign = "";
        for (CourseAndUser courseAndUser : courseAndUsers) {
            ids.add(courseAndUser.getUserId());
            unSign += courseAndUser.getUserId()+",";
        }

        log.info(unSign+"*******************************");

        if (ids.isEmpty()) {
            return JsonResult.errorException("课程中没有学生,请添加学生后再发布");
        }
        long diff = endTime.getTime() - createTime.getTime();
        Map<String, Date> maps = new HashMap<>();
        maps.put("createTime",createTime);
        maps.put("endTime",endTime);
        redisCache.setCacheObject("signDetailInfo"+id,maps);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        redisCache.setCacheList("courseId"+id,ids);
        redisCache.expire("courseId"+id, minutes, TimeUnit.MINUTES);

        LambdaQueryWrapper<CourseAndUser> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(CourseAndUser::getCourseId,id);
        Integer integer = courseAndUserMapper.selectCount(wrapper);

        int insert = signMapper.insert(new Sign(null, id, createTime, endTime, null, integer,unSign,null));
        return insert > 0 ? JsonResult.ok("已添加") : JsonResult.errorException("服务器错误");
    }

    @Override
    public JsonResult getFiles(Integer courseId, Integer nodePage, Integer pageSize) {
        if (courseId == null || nodePage <=0 || pageSize <=0) {
            return JsonResult.errorException("数据填写错误");
        }
        Page<UploadFile> page = new Page<>(nodePage, pageSize);
        LambdaQueryWrapper<UploadFile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UploadFile::getCourseId, courseId);
        Page<UploadFile> page1 = uploadFileMapper.selectPage(page, wrapper);
        return page1.getSize() == 0 ? JsonResult.errorException("无数据") : JsonResult.ok(page1);
    }

    @Override
    public JsonResult getSignDetailInfo(Integer id) {
        if (id == null) {
            return JsonResult.errorException("数据不能为空");
        }
        Map<String,Date> cacheObject = redisCache.getCacheObject("signDetailInfo" + id);

        return JsonResult.ok(cacheObject);
    }

    @Override
    public JsonResult getCourseInfo(Integer id) {
        if (id == null) {
            return JsonResult.errorException("数据不能为空");
        }
        Course course = courseMapper.selectById(id);
        return course == null ? JsonResult.errorException("无数据") : JsonResult.ok(course);
    }

    @Override
    public JsonResult getSignRecords(Integer courseId, Integer nodePage, Integer pageSize) {
        if (courseId==null) {
            return JsonResult.errorException("数据不能为空");
        }
        LambdaQueryWrapper<Sign> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Sign::getCourseId,courseId).orderByDesc(Sign::getCreateTime);
        Page<Sign> page = new Page<>(nodePage, pageSize);
        Page<Sign> page1 = signMapper.selectPage(page, wrapper);
        return page1.getSize() == 0 ? JsonResult.errorException("无数据") : JsonResult.ok(page1);
    }

    @Override
    public JsonResult deleteRecords(List<Integer> ids) {
        if (ids.isEmpty()) {
            return JsonResult.errorException("数据不能为空");
        }
        LambdaQueryWrapper<Sign> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Sign::getId,ids);
        int delete = signMapper.delete(wrapper);
        return delete > 0 ? JsonResult.ok("已删除") : JsonResult.errorException("服务器错误");
    }
}
