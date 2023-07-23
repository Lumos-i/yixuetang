package com.classroomassistant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.classroomassistant.mapper.*;
import com.classroomassistant.pojo.*;
import com.classroomassistant.service.TeacherService;
import com.classroomassistant.util.JsonResult;
import com.classroomassistant.util.RedisCache;
import com.classroomassistant.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.nio.file.Watchable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zrq
 * @ClassName TeacherServiceImpl
 * @date 2023/1/14 15:03
 * @Description TODO
 */
@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CourseAndUserMapper courseAndUserMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GradeMapper gradeMapper;
    @Autowired
    private PerformanceMapper performanceMapper;
    @Autowired
    private RemindMapper remindMapper;
    @Autowired
    private SignMapper signMapper;

    @Override
    public JsonResult myCourse(Integer nodePage, Integer pageSize) {
        if (nodePage <= 0) {
            return JsonResult.errorException("数据异常");
        }
        if (pageSize <= 0) {
            return JsonResult.errorException("数据异常");
        }
        Integer userId = UserUtils.getUserId();
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Course::getCreatorId, userId).eq(Course::getIsDeleted, 0).orderByDesc(Course::getCreateTime);
        Page<Course> page = new Page<>(nodePage, pageSize);
        Page<Course> coursePage = courseMapper.selectPage(page, wrapper);
        return page.getRecords() == null ? JsonResult.errorException("无数据") : JsonResult.ok(coursePage);
    }

    @Override
    public JsonResult deleteStudentFromCourse(Integer courseId, Integer studentId) {
        if (courseId == null || studentId == null) {
            return JsonResult.errorException("数据不能为空");
        }

        LambdaQueryWrapper<CourseAndUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseAndUser::getCourseId, courseId).eq(CourseAndUser::getUserId, studentId);

        int delete = courseAndUserMapper.delete(wrapper);
        return delete > 0 ? JsonResult.ok("已删除") : JsonResult.errorException("服务器错误");
    }

    @Override
    public JsonResult getCourseSignInfo(Integer signId,  Integer gradeId) {
        if (signId == null) {
            return JsonResult.errorException("数据不能为空");
        }
        Sign sign = signMapper.selectById(signId);
        //已签到人员id
        List<User> list1 = new ArrayList<>();
        //未签到人员id
        List<User> list2 = new ArrayList<>();
        if (gradeId == null) {
            for (String s : sign.getSignedIds().split(",")) {
                User user = userMapper.selectById(s);
                list1.add(user);
            }
            for (String s : sign.getUnsignedIds().split(",")) {
                User user = userMapper.selectById(s);
                list2.add(user);
            }
        }

        try {
            for (String s : sign.getSignedIds().split(",")) {
                User user = userMapper.selectById(s);
                if (gradeId!=null&& gradeId.equals(user.getGradeId())) {
                    list1.add(user);
                }
            }
            for (String s : sign.getUnsignedIds().split(",")) {
                User user = userMapper.selectById(s);
                if (gradeId!=null&& gradeId.equals(user.getGradeId())) {
                    list2.add(user);
                }
            }
        } catch (NullPointerException e) {
//            throw new RuntimeException(e);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("signInfo",sign);
        map.put("signUsers",list1);
        map.put("unSignInfo",list2);

        return JsonResult.ok(map);
//        if (courseId == null) {
//            return JsonResult.errorException("数据不能为空");
//        }
//        List<Integer> signed = redisCache.getCacheList("signed" + courseId);
//
////        LambdaQueryWrapper<User> wrapper0 = new LambdaQueryWrapper<>();
////        wrapper0.eq(gradeId != null, User::getGradeId, gradeId);
////        List<User> list = userMapper.selectList(wrapper0);
////        List<Integer> ids = new ArrayList<>();
////        for (User user : list) {
////            ids.add(user.getStudentId());
////        }
//
//        LambdaQueryWrapper<CourseAndUser> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(CourseAndUser::getCourseId, courseId);
//        List<CourseAndUser> courseAndUsers = courseAndUserMapper.selectList(wrapper);
//
//        List<Integer> all = new ArrayList<>();
//        for (CourseAndUser courseAndUser : courseAndUsers) {
//            all.add(courseAndUser.getUserId());
//        }
//        List<User> signedUsers = null;
//        List<User> unSignUsers = null;
//
//        List<Integer> unSigned = all.stream().filter(o -> !signed.contains(o)).collect(Collectors.toList());
//
//        if (!signed.isEmpty()) {
//            LambdaQueryWrapper<User> wrapper1 = new LambdaQueryWrapper<>();
//            wrapper1.in(User::getStudentId, signed);
//            signedUsers = userMapper.selectList(wrapper1);
//        }
//
//        if (!unSigned.isEmpty()) {
//            LambdaQueryWrapper<User> wrapper2 = new LambdaQueryWrapper<>();
//            wrapper2.in(User::getStudentId, unSigned);
//            unSignUsers = userMapper.selectList(wrapper2);
//        }
//
//        Map<String, Object> map = new HashMap<>();
//        try {
//            if (gradeId != null) {
//                List<User> list1 = new ArrayList<>();
//                if (gradeMapper.selectById(gradeId) == null) {
//                    return JsonResult.errorException("无数据");
//                }
//
//                try {
//                    for (User signedUser : signedUsers) {
//                        if (signedUser.getGradeId().equals(gradeId)) {
//                            list1.add(signedUser);
//                        }
//                    }
//                } catch (NullPointerException e) {
////                    list1 == null;
//                }
//                signedUsers = list1;
//                List<User> list2 = new ArrayList<>();
//                list2.clear();
//                for (User unSignUser : unSignUsers) {
//                    if (unSignUser.getGradeId().equals(gradeId)) {
//                        list2.add(unSignUser);
//                    }
//                }
//                unSignUsers  = list2;
//            }
//
//            map.put("signedUser", signedUsers);
//            map.put("unSignUser", unSignUsers);
//            map.put("signedUserNums", signedUsers.size());
//            map.put("unSignUserNums", unSignUsers.size());
//            String signedUserIds =  null;
//            for (User signedUser : signedUsers) {
//                signedUserIds += signedUser.getStudentId();
//            }
//            String unSignedUserIds =  null;
//            for (User unSignedUser : unSignUsers) {
//                signedUserIds += unSignedUser.getStudentId();
//            }
//            LambdaQueryWrapper<Sign> wrapper1 = new LambdaQueryWrapper<>();
//            wrapper1.eq(Sign::getCourseId,courseId).orderByDesc(Sign::getCreateTime).last("limit 1");
//            Integer id = signMapper.selectOne(wrapper1).getId();
//            signMapper.updateById(new Sign(id,null,null,null,signedUsers.size(),unSignUsers.size(),unSignedUserIds,signedUserIds));
//        } catch (NullPointerException e) {
////            return JsonResult.errorException("无数据");
//        }
//        return JsonResult.ok(map);
    }

    @Override
    public JsonResult randomName(Integer id) {
        if (id == null) {
            return JsonResult.errorException("数据不能为空");
        }
        LambdaQueryWrapper<CourseAndUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseAndUser::getCourseId, id);
        List<CourseAndUser> courseAndUsers = courseAndUserMapper.selectList(wrapper);
        Random random = new Random();
        int n = random.nextInt(courseAndUsers.size());
        CourseAndUser courseAndUser = courseAndUsers.get(n);

        User user = userMapper.selectById(courseAndUser.getUserId());
        redisCache.setCacheObject("randomName" + id, user);
        return user == null ? JsonResult.errorException("数据为空") : JsonResult.ok(user);
    }

    @Override
    public JsonResult grouping(Integer courseId, Integer studentNums, Integer sex, Integer performance) {
        if (courseId == null) {
            return JsonResult.errorException("请选择课程");
        }
//        LambdaQueryWrapper<CourseAndUser> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(CourseAndUser::getCourseId, courseId);
//        List<CourseAndUser> courseAndUsers = courseAndUserMapper.selectList(wrapper);
//        List<User> allUser = new ArrayList<>();
//        for (CourseAndUser courseAndUser : courseAndUsers) {
//            allUser.add(userMapper.selectById(courseAndUser.getUserId()));
//        }
        List<User> man = userMapper.grouping1("男", courseId);
        List<User> woman = userMapper.grouping1("女", courseId);
        List<User> all = new ArrayList<>(man);
        all.addAll(woman);
        List<User> man1 = userMapper.grouping2("男", courseId);
        List<User> woman1 = userMapper.grouping2("女", courseId);
        List<User> all1 = new ArrayList<>(man1);
        all1.addAll(woman1);
        int groupCount = (int) Math.ceil(all.size() / studentNums);
        int res = all.size() % studentNums;
        Set<User> set = new HashSet<>();
        Map<String, Set<User>> map = new HashMap<>();

        if ((sex == 1 && performance == 1)) {
            Set<User> set1 = new HashSet<>();
            int n = 0;
            while (n != groupCount) {
                set1.clear();
                for (int i = 1; i <= studentNums; i++) {
                    if (man.size() > 0) {
                        User user = man.get(0);
                        man.remove(0);
                        set1.add(user);
                    } else {
                        if (woman.isEmpty()) {
                            return JsonResult.ok(map);
                        }
                        User user = woman.get(0);
                        woman.remove(0);
                        set1.add(user);
                    }
                }
                n++;
                map.put("group" + n + ":", set1);
            }
        } else if ((sex == 1 && performance == 2)) {
            int n = 0;
            while (n <= groupCount) {
                set.clear();
                for (int i = 1; i <= studentNums; i++) {
                    if (man1.size() > 0) {
                        User user = man1.get(0);
                        set.add(user);
                        man1.remove(0);
                    } else {
                        if (woman1.isEmpty()) {
                            return JsonResult.ok(map);
                        }
                        User user = woman1.get(0);
                        set.add(user);
                        woman1.remove(0);
                    }
                }
                n++;
                map.put("group" + n + ":", set);

            }
        } else if ((sex == 2 && performance == 1)) {
            int n = 0;
            Random rand = new Random();
            while (n <= groupCount) {
                set.clear();
                for (int i = 0; i < studentNums; i++) {
                    int b = rand.nextInt(all1.size());
                    set.add(all1.get(b));
                    all1.remove(b);
                    if (all1.size() == 0) {
                        map.put("group" + n + ":", set);
                        break;
                    }
                }
                n++;
                map.put("group" + n + ":", set);
            }
        } else if ((sex == 2 && performance == 2)) {
            int n = 0;
            Random rand = new Random();
            while (n != groupCount) {
                set.clear();
                for (int i = 1; i <= studentNums; i++) {
                    int b = rand.nextInt(all.size());
                    System.out.println(all.get(b));
                    all.remove(b);
                }
                n++;
                map.put("group" + n + ":", set);
            }
        }
//        List<User> list = new ArrayList<>();
//        for(Set<User> user:map.values()){
//            list.addAll(user);
//        }
//
//        all1 = all.stream().filter(e -> {
//            return !list.contains(e);
//        }).collect(Collectors.toList());
//
//        log.info(all1.size()+"**********************");
//
//        if(res != 0) {
//           if (sex == 2 && performance == 1) {
//               for (int i = 0; i < all1.size(); i++) {
//                   Set<User> set1 = map.get("group" + (i + 1) + ":");
//                   set1.add(list.get(i));
//                   map.put("group" + (i + 1) + ":",set1);
//               }
//           }else {
//               for (int i = 0; i < all.size(); i++) {
//                   Set<User> set1 = map.get("group" + (i + 1) + ":");
//                   set1.add(list.get(i));
//                   map.put("group" + (i + 1) + ":",set1);
//               }
//           }
//        }

        redisCache.setCacheObject("groups:" + courseId, map);
        return map.containsKey("group1:") ? JsonResult.ok(map) : JsonResult.errorException("出错了");
    }

    @Override
    public JsonResult announcement(Integer courseId, String content) {
        if (courseId == null || StringUtils.isEmpty(content)) {
            return JsonResult.errorException("数据不能为空");
        }
        Integer userId = UserUtils.getUserId();
        int insert = remindMapper.insert(new Remind(null, userId, courseId, content, 2, null, null));
        return insert > 0 ? JsonResult.ok() : JsonResult.errorException("服务器错误");
    }

    @Override
    public JsonResult addPerformance(Integer userId, Integer courseId, BigDecimal performance,Integer termId) {
        if (userId == null || courseId == null || performance.equals("")) {
            return JsonResult.errorException("数据不能为空");
        }
        int insert = performanceMapper.insert(new Performance(null, courseId, userId, performance, null, 0,termId));
        return insert > 0 ? JsonResult.ok() : JsonResult.errorException("服务器错误");
    }

    @Override
    public JsonResult groupingAnd(Boolean sex, Boolean performance, Integer courseId, Integer studentNums) {
        Map<String, Set<User>> map = new HashMap<>();
        if (StringUtils.isEmpty(sex) ? false : sex) {
            List<User> man = userMapper.grouping1("男", courseId);
            List<User> woman = userMapper.grouping1("女", courseId);
            List<Set<User>> divide = divide(man, studentNums);
            List<Set<User>> woDivide = divide(woman, studentNums);
            for (int i = 0; i < divide.size(); i++) {
                map.put("group" + (i + 1), divide.get(i));
            }
            for (int i = 0; i < woDivide.size(); i++) {
                map.put("group" + (i + 1 + divide.size()), woDivide.get(i));
            }
        } else if (StringUtils.isEmpty(performance) ? false : performance) {
            QueryWrapper<Performance> performanceQueryWrapper = new QueryWrapper<>();
            performanceQueryWrapper.select("user_id")
                    .eq("course_id", courseId)
                    .orderByDesc("performance");
            List<User> userList = new ArrayList<>();
            performanceMapper.selectList(performanceQueryWrapper).forEach(performance1 -> {
                userList.add(userMapper.selectById(performance1.getUserId()));
            });
            List<Set<User>> divide = divide(userList, studentNums);
            for (int i = 0; i < divide.size(); i++) {
                map.put("group" + (i + 1), divide.get(i));
            }
        }
        redisCache.setCacheObject("groups:" + courseId, map);
        return JsonResult.ok(map);
    }

    public List<Set<User>> divide(List<User> list, Integer count) {
        Integer size = list.size();
        if (size==0){
            return new ArrayList<>();
        }
        Integer count1 = size / count;
        if (count*count1<size){
            count1++;
        }
        List<Set<User>> list1 = new ArrayList<>();
        if (size <= count) {
            Set<User> set = new HashSet<>(list);
            list1.add(set);
            return list1;
        } else {
            for (int i = 0; i < count1; i++) {
                Set<User> set = new HashSet<>();
                for (int j = i * count; j < (i + 1) * count; j++) {
                    if (j>=size){
                        break;
                    }
                    set.add(list.get(j));
                }
                list1.add(set);
            }
            return list1;
        }
    }

}
