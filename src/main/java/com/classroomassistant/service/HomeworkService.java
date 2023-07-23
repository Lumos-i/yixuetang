package com.classroomassistant.service;

import com.classroomassistant.pojo.Correct;
import com.classroomassistant.pojo.Homework;
import com.classroomassistant.pojo.Task;
import com.classroomassistant.pojo.UserAnswer;
import com.classroomassistant.util.JsonResult;
import org.springframework.web.multipart.MultipartFile;

public interface HomeworkService {
    JsonResult createHomework(Homework homework);

    JsonResult updateHomework(Homework homework);

    JsonResult deleteHomework(Integer homeworkId);

    JsonResult getHomework(Integer beginIndex, Integer size, Integer courseId);

    JsonResult submitAnswer(UserAnswer userAnswer);

    JsonResult correctHomework(Correct correct);

    JsonResult getOutcome();

    JsonResult publishTask(Task task);

    JsonResult deleteTask(Integer taskId);

    JsonResult getTask(Integer courseId, Integer beginIndex, Integer size);

    JsonResult getHomeworkById(Integer homeworkId, Integer studentId);

    JsonResult publishQuestion(String union, String question, Integer time, String answer);

    JsonResult publishAnswer(String union, String answer);

    JsonResult getCourseOutCome(String union);

    JsonResult addPicture(MultipartFile file);

    JsonResult deletePicture(String fileName);

    JsonResult getTaskInfoById(Integer taskId, Integer gradeId, String sex, Integer status, Integer beginIndex, Integer size);

    JsonResult getCourseQuestion(String union);
}
