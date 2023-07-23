package com.classroomassistant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.classroomassistant.mapper.GradeMapper;
import com.classroomassistant.mapper.UserAndClassMapper;
import com.classroomassistant.mapper.UserMapper;
import com.classroomassistant.pojo.Grade;
import com.classroomassistant.pojo.User;
import com.classroomassistant.pojo.UserAndClass;
import com.classroomassistant.service.GradeService;
import com.classroomassistant.util.JsonResult;
import com.classroomassistant.util.RedisUtil;
import com.classroomassistant.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author DELL
 **/
@Service
public class GradeServiceImp implements GradeService {
    @Autowired
    private GradeMapper gradeMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserAndClassMapper userAndClassMapper;

    @Autowired
    private UserMapper userMapper;
    @Override
    public JsonResult createGrade(String className) {
        User user=UserUtils.getLoginUser().getUser();
        if (user==null){
            return JsonResult.errorMsg("没有用户信息");
        }
        QueryWrapper<Grade> gradeQueryWrapper=new QueryWrapper<>();
        gradeQueryWrapper.select("code").eq("deleted",false);
        Set<String> set=new HashSet<>();
        gradeMapper.selectList(null).forEach(grade -> {
            set.add(grade.getCode());
        });
        String code=JsonResult.createCode(10);
        while (set.contains(code)){
            code = JsonResult.createCode(10);
        }
        int i = gradeMapper.insert(new Grade(null, className, user.getStudentId(), null, code,null));
        return i>0?JsonResult.ok(code):JsonResult.error();
    }

    @Override
    public JsonResult deleteGrade(Integer gradeId) {
        int i = gradeMapper.updateById(new Grade(gradeId, true));
        return i>0?JsonResult.ok():JsonResult.error();
    }

    @Override
    public JsonResult updateGrade(Grade grade) {
        int i = gradeMapper.updateById(grade);
        return i>0?JsonResult.ok():JsonResult.error();
    }

    @Override
    public JsonResult getGrade(Integer beginIndex, Integer size) {
        Integer userId = UserUtils.getUserId();
        QueryWrapper<Grade> gradeQueryWrapper=new QueryWrapper<>();
        gradeQueryWrapper.eq("deleted",false);
        if (UserUtils.getIdentity()==1){
            gradeQueryWrapper.eq("create_name",userId);
        }else if (UserUtils.getIdentity()==0){
            QueryWrapper<UserAndClass> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("user_id",userId);
            Integer classId = userAndClassMapper.selectOne(queryWrapper).getClassId();
            gradeQueryWrapper.eq("id",classId);
        }
        Page<Grade> grade = gradeMapper.selectPage(new Page<Grade>(beginIndex, size), gradeQueryWrapper);
        return JsonResult.ok(grade);
    }

    @Override
    public JsonResult enterGrade(String regexCode) {
        QueryWrapper<Grade> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("code",regexCode);
        Grade grade = gradeMapper.selectOne(queryWrapper);
        int insert = userAndClassMapper.insert(new UserAndClass(grade.getId(), UserUtils.getUserId()));
        User user=new User();
        user.setStudentId(UserUtils.getUserId());
        user.setGradeId(grade.getId());
        redisUtil.del("userId:"+UserUtils.getUserId());
        userMapper.updateById(user);
        return insert>0?JsonResult.ok():JsonResult.error();
    }

    @Override
    public JsonResult getGradeByCourse(Integer courseId) {
        List<Grade> gradeByCourse = gradeMapper.getGradeByCourse(courseId);
        Set<Grade> set=new HashSet<>(gradeByCourse);
        return JsonResult.ok(set);
    }

    @Override
    public JsonResult getAllGrade() {
        List<Grade> grades = gradeMapper.selectList(null);
        return JsonResult.ok(grades);
    }
}
