package com.classroomassistant.service;

import com.classroomassistant.util.JsonResult;
import com.classroomassistant.vo.UserDto;
import com.classroomassistant.vo.UserInfoVo;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * @author zrq
 * @ClassName UserService
 * @date 2023/1/11 17:51
 * @Description TODO
 */
public interface UserService {
    JsonResult register(String username, String password, String email, String code);

    JsonResult sendCode(String email);

    JsonResult login(String username, String password);

    JsonResult logOut();

    JsonResult updatePhoto(MultipartFile photo);

    JsonResult updateUserInfo(UserInfoVo userInfoVo);

    JsonResult forgetPassword(String email, String code, String newPassword);

    JsonResult deleteUser();

    JsonResult enterCourseByCode(String code);

    JsonResult courseStudents(Integer classId,@RequestParam(value = "nodePage", defaultValue = "1") Integer nodePage,
                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize);

    JsonResult getAllUser(Integer nodePage, Integer pageSize, Integer gradeId, String sex);

    JsonResult deleteUser1(Integer id);




    JsonResult getUserInfo();

    JsonResult sign(Integer signId, Integer courseId);

    JsonResult updatePassword(String email, String code, String newPassword);

    JsonResult uploadFile(Integer courseId, String fileName, MultipartFile fileAddress, Date createTime);

    JsonResult updateUserInfoById(UserInfoVo userInfoVo,Integer id);

    JsonResult resetPassword(Integer id);

    JsonResult allAddress();

    JsonResult sendMessage(String bzid, String message);

    JsonResult getRandomName(Integer courseId);

    JsonResult getTeacherInfo(Integer id);

    JsonResult groupInfo(Integer courseId);

    JsonResult sendMessage2(String content, String groupId, Integer courseId);

    JsonResult getMessageHistory(Integer courseId, String groupId, Integer nodePage, Integer pageSize);

    JsonResult addTerm(Date beginTime, Date endTime, String name);

    JsonResult getTerm();

    JsonResult whetherSign(Integer courseId);

    JsonResult deleteFile(Integer id);

    JsonResult myGrouping(Integer courseId);

    JsonResult getGroupById(Integer courseId);

    JsonResult updatePower(Integer id, Integer power);

    JsonResult getCourseSign(Integer courseId, Integer nodePage, Integer pageSize);
}
