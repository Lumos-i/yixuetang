package com.classroomassistant.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.classroomassistant.mapper.*;
import com.classroomassistant.pojo.*;
import com.classroomassistant.service.HomeworkService;
import com.classroomassistant.util.*;
import com.classroomassistant.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author DELL
 **/
@Service
public class HomeworkServiceImpl implements HomeworkService {
    @Autowired
    private HomeworkMapper homeworkMapper;
    @Autowired
    private CorrectMapper correctMapper;
    @Autowired
    private UserAnswerMapper userAnswerMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private CourseAndUserMapper courseAndUserMapper;
    @Autowired
    private CosUtil cosUtil;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private GradeMapper gradeMapper;
    @Autowired
    private UserMapper userMapper;


    @Override
    public JsonResult createHomework(Homework homework) {
        Integer userId = UserUtils.getUserId();
        homework.setTeacherId(userId);
        int i = homeworkMapper.insert(homework);
        QueryWrapper<Homework> homeworkQueryWrapper=new QueryWrapper<>();
        homeworkQueryWrapper.eq("question",homework.getQuestion());
        List<Homework> homeworks = homeworkMapper.selectList(homeworkQueryWrapper);
        return i>0?JsonResult.ok(homeworks):JsonResult.error();
    }

    @Override
    public JsonResult updateHomework(Homework homework) {
        int i = homeworkMapper.updateById(homework);
        return i>0?JsonResult.ok():JsonResult.error();
    }

    @Override
    public JsonResult deleteHomework(Integer homeworkId) {
        int i = homeworkMapper.updateById(new Homework(homeworkId,true));
        return i>0?JsonResult.ok():JsonResult.error();
    }

    @Override
    public JsonResult getHomework(Integer beginIndex, Integer size, Integer courseId) {
        Integer identity = UserUtils.getIdentity();
        QueryWrapper<Homework> homeworkQueryWrapper=new QueryWrapper<>();
        homeworkQueryWrapper.eq("deleted",false);
        QueryWrapper<Task> taskQueryWrapper=new QueryWrapper<>();
        taskQueryWrapper.eq("deleted",false)
                .eq(!StringUtils.isEmpty(courseId),"belong_course_id",courseId);
        List<Task> tasks = taskMapper.selectList(taskQueryWrapper);
        if (identity==1){
            homeworkQueryWrapper.eq("teacher_id",UserUtils.getUserId());
        }else if (identity == 0){
            List<Integer> homeworkIds=new ArrayList<>();
            tasks.forEach(task -> {
                homeworkIds.add(task.getHomeworkId());
            });
            homeworkQueryWrapper.in("id",homeworkIds);
        }
        Page<Homework> homework = homeworkMapper.selectPage(new Page<Homework>(beginIndex, size), homeworkQueryWrapper);
        return JsonResult.ok(homework);
    }

    @Override
    public JsonResult submitAnswer(UserAnswer userAnswer) {
        userAnswerMapper.insert(userAnswer);
        Homework homework = homeworkMapper.selectById(userAnswer.getHId());
        List<Answer> homeworkAnswer = homework.getAnswer();
        List<Answer> answerUserAnswer = userAnswer.getUserAnswer();
        BigDecimal temp=new BigDecimal("0");
        List<Description> descriptions=new ArrayList<>();
        Boolean flag=true;
        int correct=0;
        for (int i=0;i<homework.getQuestionCount();i++){
            Question question= (Question) getJsonConvern(Question.class,homework.getQuestion().get(i));
            BigDecimal questionScore = question.getScore();
            Answer hAnswer = (Answer) getJsonConvern(Answer.class, homeworkAnswer.get(i));
            if (hAnswer.getType()<=4){
                Answer answer = (Answer) getJsonConvern(Answer.class, answerUserAnswer.get(i));
                if (hAnswer.getAnswer().equals(answer.getAnswer())){
                    temp=temp.add(questionScore);
                    correct++;
                    descriptions.add(new Description(i,questionScore,questionScore));
                }else {
                    descriptions.add(new Description(i,BigDecimal.ZERO,questionScore));
                }
            }else {
                flag=false;
                descriptions.add(new Description(i,null,questionScore));
            }
        }
        Correct end=new Correct(null,homework.getId(),UserUtils.getUserId(),temp,descriptions,flag?temp:null,homework.getQuestionCount(),correct);
        correctMapper.insert(end);

        return JsonResult.ok(end);
    }

    public Object getJsonConvern(Class name,Object o){
        String s = JSON.toJSONString(o);
        return JSONObject.parseObject(s,name);
    }

    @Override
    public JsonResult correctHomework(Correct correct) {
        int i = correctMapper.updateById(correct);
        return i>0?JsonResult.ok():JsonResult.error();
    }

    @Override
    public JsonResult getOutcome() {
        QueryWrapper<Homework> homeworkQueryWrapper=new QueryWrapper<>();
        homeworkQueryWrapper.eq("teacher_id",UserUtils.getUserId()).eq("deleted",false);
        List<Homework> homeworks = homeworkMapper.selectList(homeworkQueryWrapper);
        List<Analysis> analyses=new ArrayList<>();
        for (Homework homework : homeworks) {
            QueryWrapper<Correct> correctQueryWrapper=new QueryWrapper<>();
            correctQueryWrapper.eq("h_id",homework.getId());
            List<Correct> corrects = correctMapper.selectList(correctQueryWrapper);
            BigDecimal zero=BigDecimal.ZERO;
            for (Correct correct : corrects) {
                zero =zero.add(correct.getAllScore());
            }
            BigDecimal divide = zero.divide(BigDecimal.valueOf(corrects.size()));
            List<AvgData> avgDatas = correctMapper.getAvgData(homework.getId());
            analyses.add(new Analysis(homework,corrects.size(),divide,avgDatas));
        }

        return JsonResult.ok(analyses);
    }



    @Override
    public JsonResult publishTask(Task task) {
        task.setCreateUserId(UserUtils.getUserId());
        int i = taskMapper.insert(task);
        return i>0?JsonResult.ok():JsonResult.error();
    }

    @Override
    public JsonResult deleteTask(Integer taskId) {
        return taskMapper.updateById(new Task(taskId,true))>0?JsonResult.ok():JsonResult.error();
    }

    @Override
    public JsonResult getTask(Integer courseId, Integer beginIndex, Integer size) {
        QueryWrapper<Task> taskQueryWrapper=new QueryWrapper<>();
        taskQueryWrapper.eq(!StringUtils.isEmpty(courseId),"belong_course_id",courseId).eq("deleted",false);
        PageUtil pageUtil=new PageUtil<>();
        List<Task> taskPage = taskMapper.selectList(taskQueryWrapper);
        List<TaskInfo> list=new ArrayList<>();
        if (UserUtils.getIdentity()==0){

            Integer userId=UserUtils.getUserId();
            taskPage.forEach(task -> {
                QueryWrapper<UserAnswer> queryWrapper=new QueryWrapper<>();
                QueryWrapper<Correct> correctQueryWrapper=new QueryWrapper<>();

                queryWrapper.eq("h_id",task.getHomeworkId())
                        .eq("user_id",userId);
                correctQueryWrapper.eq("h_id",task.getHomeworkId())
                        .eq("user_id",userId);

                UserAnswer userAnswer = userAnswerMapper.selectOne(queryWrapper);
                Correct correct = correctMapper.selectOne(correctQueryWrapper);

                list.add(new TaskInfo(task,correct,userAnswer,null,null));
            });
        }else {
            taskPage.forEach(task -> {
                QueryWrapper<CourseAndUser> courseAndUserQueryWrapper=new QueryWrapper<>();
                courseAndUserQueryWrapper.eq("course_id",task.getBelongCourseId());
                Integer allCount = courseAndUserMapper.selectCount(courseAndUserQueryWrapper);
                List<User> finishHomework = userAnswerMapper.getFinishHomework(task.getBelongCourseId());
                list.add(new TaskInfo(task,null,null,allCount, finishHomework.size()));
            });
        }
        return JsonResult.ok(pageUtil.getCount(list,beginIndex,size));
    }

    @Override
    public JsonResult getHomeworkById(Integer homeworkId, Integer studentId) {
        QueryWrapper<Correct> correctQueryWrapper=new QueryWrapper<>();
        QueryWrapper<UserAnswer> answerQueryWrapper=new QueryWrapper<>();

        answerQueryWrapper.eq("h_id",homeworkId)
                .eq("user_id",StringUtils.isEmpty(studentId)?UserUtils.getUserId():studentId);
        correctQueryWrapper.eq("h_id",homeworkId)
                .eq("user_id",StringUtils.isEmpty(studentId)?UserUtils.getUserId():studentId);

        Homework homework = homeworkMapper.selectById(homeworkId);
        UserAnswer userAnswer = userAnswerMapper.selectOne(answerQueryWrapper);
        Correct correct = correctMapper.selectOne(correctQueryWrapper);

        return JsonResult.ok(new UserHomework(homework,userAnswer,correct));
    }

    @Override
    public JsonResult publishQuestion(String union, String question, Integer time, String answer) {
        redisCache.setCacheObject(union+":question",question,time, TimeUnit.SECONDS);
        redisCache.setCacheObject(union+":answer",answer,time, TimeUnit.MINUTES);
        redisCache.setCacheObject(union+":allCount",0,time, TimeUnit.MINUTES);
        redisCache.setCacheObject(union+":trueCount",0,time, TimeUnit.MINUTES);
        redisCache.setCacheObject(union+":manCount",0,time, TimeUnit.MINUTES);
        redisCache.setCacheObject(union+":womanCount",0,time, TimeUnit.MINUTES);
        return JsonResult.ok();
    }

    @Override
    public JsonResult publishAnswer(String union, String answer) {
        String userAnswer = redisCache.getCacheObject(union + ":answer");
        redisUtil.incr(union+":allCount",1);
        User user = UserUtils.getLoginUser().getUser();
        if (userAnswer.equals(answer)){
            redisUtil.incr(union+":trueCount",1);
            if (user.getSex().equals("男")){
                redisUtil.incr(union+":manCount",1);
            }else {
                redisUtil.incr(union+":womanCount",1);
            }
            Map<String, Object> map=new HashMap<>(redisCache.getCacheMap(union+":grade"));
            Grade grade = gradeMapper.selectById(user.getGradeId());
            map.put(grade.getClassName(),StringUtils.isEmpty(redisCache.getCacheMapValue(union+":grade", grade.getClassName()))?1:(Integer)redisCache.getCacheMapValue(union+":grade", grade.getClassName())+1);
            redisUtil.hmset(union+":grade",map);
        }
        return JsonResult.ok();
    }

    @Override
    public JsonResult getCourseOutCome(String union) {
        Integer allCount = redisCache.getCacheObject(union + ":allCount");
        Integer manCount = redisCache.getCacheObject(union + ":manCount");
        Integer womanCount = redisCache.getCacheObject(union + ":womanCount");
        Map<String, Integer> map = redisCache.getCacheMap(union + ":grade");
        return JsonResult.ok(new QuestionOutCome(allCount,manCount,womanCount,map));
    }

    @Override
    public JsonResult getCourseQuestion(String union) {
        String question = redisCache.getCacheObject(union + ":question");
        return JsonResult.ok(question);
    }

    @Override
    public JsonResult addPicture(MultipartFile file) {
        return JsonResult.ok(cosUtil.upload(file,2));
    }

    @Override
    public JsonResult deletePicture(String fileName) {
        cosUtil.deleteObject(fileName,2);
        return JsonResult.ok();
    }

    @Override
    public JsonResult getTaskInfoById(Integer taskId, Integer gradeId, String sex, Integer status, Integer beginIndex, Integer size) {
        Task task = taskMapper.selectById(taskId);
        QueryWrapper<CourseAndUser> courseAndUserQueryWrapper=new QueryWrapper<>();
        courseAndUserQueryWrapper.eq("course_id",task.getBelongCourseId());
        List<User> users=new ArrayList<>();
        courseAndUserMapper.selectList(courseAndUserQueryWrapper).forEach(courseAndUser -> {
            QueryWrapper<User> userQueryWrapper=new QueryWrapper<>();
            userQueryWrapper.eq("id",courseAndUser.getUserId())
                    .eq(!StringUtils.isEmpty(gradeId),"grade_id",gradeId)
                    .eq(!StringUtils.isEmpty(sex),"sex",sex);
            User user = userMapper.selectOne(userQueryWrapper);
            if (!StringUtils.isEmpty(user)){
                QueryWrapper<UserAnswer> userAnswerQueryWrapper=new QueryWrapper<>();
                userAnswerQueryWrapper.eq("user_id",user.getStudentId())
                        .eq("h_id",task.getHomeworkId());
                Integer count = userAnswerMapper.selectCount(userAnswerQueryWrapper);
                if (!StringUtils.isEmpty(status)){
                    if (status==1 && count>0){
                        users.add(user);
                    }else if (status==2 && count==0){
                        users.add(user);
                    }
                }else {
                    users.add(user);
                }

            }

        });
        PageUtil pageUtil=new PageUtil<>();
        PageUtil count = pageUtil.getCount(users, beginIndex, size);

        return JsonResult.ok(count);
    }


}
