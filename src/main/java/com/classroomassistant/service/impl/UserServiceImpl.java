package com.classroomassistant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.classroomassistant.config.WsServer;
import com.classroomassistant.dto.UserInfoDto;
import com.classroomassistant.mapper.*;
import com.classroomassistant.pojo.*;
import com.classroomassistant.service.UserService;
import com.classroomassistant.util.*;
import com.classroomassistant.vo.UserInfoVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.classroomassistant.util.ValidationUtil.EMAIL;
import static com.classroomassistant.util.ValidationUtil.QQ_EMAIL;

/**
 * @author zrq
 * @ClassName UserServiceImpl
 * @date 2023/1/11 17:52
 * @Description TODO
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private TermMapper termMapper;
    @Autowired
    private CosUtil cosUtil;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private GradeMapper gradeMapper;
    @Autowired
    private CourseAndUserMapper courseAndUserMapper;
    @Autowired
    private SendMailVerify sendMailVerify;
    @Autowired
    private HomeworkMapper homeworkMapper;
    @Autowired
    private CorrectMapper correctMapper;
    @Autowired
    private UserAnswerMapper userAnswerMapper;
    @Autowired
    private UserAndClassMapper userAndClassMapper;
    @Autowired
    private UploadFileMapper uploadFileMapper;
    @Autowired
    private GroupChatMapper groupChatMapper;

    @Autowired
    private SignMapper signMapper;


    @PostMapping("/register")
    @ApiModelProperty("注册")
    @Override
    public JsonResult register(String username, String password, String email, String code) {
        log.info(email + "," + code);
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(email) || StringUtils.isEmpty(code)) {
            return JsonResult.errorException("数据不能为空");
        }
        if (StringUtils.isEmpty(redisCache.getCacheObject(email))) {
            return JsonResult.errorException("验证码已失效");
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName, username).or().eq(User::getEmail, email);
        User user1 = userMapper.selectOne(wrapper);
        if (!Objects.isNull(user1)) {
            return JsonResult.errorException("账号或邮箱已存在");
        }
        if (!RegExpUtil.matchUsername(username)) {
            return JsonResult.errorException("账号须为9到12位数字");
        }
        if (!RegExpUtil.matchPassword(password)) {
            return JsonResult.errorException("密码须含至少一个字母且密码长度位6到16位");
        }
        if (code.equals(redisCache.getCacheObject(email).toString()) || (redisCache.getCacheObject(email).toString()).equals(code)) {
            if (RegExpUtil.matchEmail(email)) {
                User user = new User();
                user.setUserName(username);
                user.setPassword(new BCryptPasswordEncoder().encode(password));
                user.setEmail(email);
                return userMapper.insert(user) > 0 ? JsonResult.ok("注册成功") : JsonResult.errorException("服务器错误");
            } else {
                return JsonResult.errorException("邮箱格式错误");
            }
        } else {
            return JsonResult.errorException("验证码错误");
        }
    }

    @Override
    public JsonResult sendCode(String email) {
        if ("".equals(email) || StringUtils.isEmpty(email)) {
            return JsonResult.errorException("数据为空");
        }
        if (!email.matches(QQ_EMAIL) && !email.matches(EMAIL)) {
            return JsonResult.errorException("格式不正确");
        }
        if (redisCache.getCacheObject("email" + email) != null) {
            return JsonResult.errorException("您的操作过于频繁，请再30s后再重试");
        }
        int code = sendMailVerify.MailVerify(email);
        redisCache.setCacheObject(email, code, 5, TimeUnit.MINUTES);
        redisCache.setCacheObject("email" + email, email, 30, TimeUnit.SECONDS);
        log.info(code + "");
        return JsonResult.ok(code);
    }

    @Override
    public JsonResult login(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return JsonResult.errorException("所填内容不能为空");
        }
        log.info(username + "," + password);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        log.info(loginUser + "");
        Integer power = loginUser.getUser().getIdentity();
        String jwt = JwtUtils.getJwtToken(username, power + "", loginUser.getUser().getStudentId());
        log.info(jwt);
        //authenticate存入redis
        redisCache.setCacheObject("login:" + username, loginUser, 60 * 24, TimeUnit.MINUTES);
        log.info(username + "已登录");
        return JsonResult.ok(jwt);
    }

    @Override
    public JsonResult logOut() {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser principal = (LoginUser) authenticationToken.getPrincipal();
        String username = principal.getUsername();
        redisCache.deleteObject("login:" + username);
        return JsonResult.ok("已退出");
    }

    @Override
    public JsonResult updatePhoto(MultipartFile photo) {
        if (!FileTypeUtil.isImage(photo)) {
            return JsonResult.errorException("图片格式错误");
        }
        //上传到cos云
        String upload = cosUtil.upload(photo, 2);
        //获得用户id
        Integer userId = UserUtils.getUserId();
        //查询用户之前的头像
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getStudentId, userId);

        String photo1 = userMapper.selectOne(wrapper).getPhoto();
        if (!StringUtils.isEmpty(photo1)) {
            cosUtil.deleteObject(photo1, 2);
        }
        User user = new User();
        user.setStudentId(userId);
        user.setPhoto(upload);
        redisCache.deleteObject("userId:" + userId);
        int update = userMapper.updateById(user);
        return update > 0 ? JsonResult.ok("已更新") : JsonResult.errorException("服务器异常");
    }

    @Override
    public JsonResult forgetPassword(String email, String code, String newPassword) {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(code) || StringUtils.isEmpty(newPassword)) {
            return JsonResult.errorException("数据不能为空");
        }
        if (StringUtils.isEmpty(redisCache.getCacheObject(email))) {
            return JsonResult.errorException("验证码已失效");
        }
        if (!code.equals(redisCache.getCacheObject(email).toString()) || !(redisCache.getCacheObject(email).toString()).equals(code)) {
            return JsonResult.errorException("验证码错误");
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        User user = new User();
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        int update = userMapper.update(user, wrapper);
        return update > 0 ? JsonResult.ok("已更新") : JsonResult.errorException("服务器异常");
    }

    @Override
    public JsonResult updateUserInfo(UserInfoVo userInfoVo) {
        if (userInfoVo == null) {
            return JsonResult.errorException("数据不能为空");
        }
        Integer userId = UserUtils.getUserId();
        User user = new User(userId, null, null, null, null, userInfoVo.getSex(), userInfoVo.getName(), userInfoVo.getGradeId(), userInfoVo.getNativePlace(), null, null, null, null);
        int update = userMapper.updateById(user);
        redisCache.deleteObject("userId:" + userId);
        return update > 0 ? JsonResult.ok("已更新") : JsonResult.errorException("服务器异常");
    }

    @Override
    public JsonResult deleteUser() {
        Integer userId = UserUtils.getUserId();
        User user = new User();
        user.setStudentId(userId);
        user.setIsDeleted(1);
        int update = userMapper.updateById(user);
        return update > 0 ? JsonResult.ok("已删除") : JsonResult.errorException("服务器异常");
    }

    @Override
    public JsonResult enterCourseByCode(String code) {
        Integer userId = UserUtils.getUserId();
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Course::getCourseCode, code);
        Integer id = courseMapper.selectOne(wrapper).getId();
        LambdaQueryWrapper<CourseAndUser> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(CourseAndUser::getCourseId,id).eq(CourseAndUser::getUserId,userId);
        if(courseAndUserMapper.selectOne(wrapper1)!=null) {
            return JsonResult.errorException("已加入该课程");
        }
        int insert = courseAndUserMapper.insert(new CourseAndUser(id, userId));
        return insert > 0 ? JsonResult.ok("已加入") : JsonResult.errorException("服务器异常");
    }

    @Override
    public JsonResult courseStudents(Integer classId, Integer nodePage, Integer pageSize) {
        if (classId == null) {
            return JsonResult.errorException("数据不能为空");
        }

        LambdaQueryWrapper<CourseAndUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseAndUser::getCourseId, classId);
        List<CourseAndUser> courseAndUsers = courseAndUserMapper.selectList(wrapper);
        List<Integer> ids = new ArrayList<>();
        for (CourseAndUser courseAndUser : courseAndUsers) {
            ids.add(courseAndUser.getUserId());
        }
        if (courseAndUsers == null) {
            return JsonResult.errorException("无数据");
        }

        Page<User> page = new Page<>(nodePage, pageSize);
        LambdaQueryWrapper<User> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.in(User::getStudentId, ids);
        Page<User> page1 = userMapper.selectPage(page, wrapper1);
        return page1.getRecords() == null ? JsonResult.errorException("无数据") : JsonResult.ok(page1);
    }


    @Override
    public JsonResult getAllUser(Integer nodePage, Integer pageSize, Integer gradeId, String sex) {
        if (nodePage <= 0) {
            return JsonResult.errorException("页数不正确");
        }
        if (pageSize <= 0) {
            return JsonResult.errorException("数据条数不正确");
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(gradeId != null, User::getGradeId, gradeId).eq(!StringUtils.isEmpty(sex), User::getSex, sex);
        Page<User> page = new Page<>(nodePage, pageSize);
        Page<User> page1 = userMapper.selectPage(page, wrapper);
        return page1.getRecords().isEmpty() ? JsonResult.errorException("无数据") : JsonResult.ok(page1);
    }

    @Override
    public JsonResult deleteUser1(Integer id) {
        if (id == null) {
            return JsonResult.errorException("数据不能为空");
        }
        LambdaQueryWrapper<Homework> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Homework::getTeacherId, id);
        homeworkMapper.delete(wrapper);
        LambdaQueryWrapper<Correct> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Correct::getUserId, id);
        correctMapper.delete(wrapper1);
        LambdaQueryWrapper<UserAnswer> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(UserAnswer::getUserId, id);
        userAnswerMapper.delete(wrapper2);
        LambdaQueryWrapper<UserAndClass> wrapper3 = new LambdaQueryWrapper<>();
        wrapper3.eq(UserAndClass::getUserId, id);
        userAndClassMapper.delete(wrapper3);
        LambdaQueryWrapper<CourseAndUser> wrapper4 = new LambdaQueryWrapper<>();
        wrapper4.eq(CourseAndUser::getUserId, id);
        courseAndUserMapper.delete(wrapper4);
        LambdaQueryWrapper<User> wrapper5 = new LambdaQueryWrapper<>();
        wrapper5.eq(User::getStudentId, id);
        LambdaQueryWrapper<Course> wrapper6 = new LambdaQueryWrapper<>();
        wrapper6.eq(Course::getCreatorId,id);
        courseMapper.delete(wrapper6);
        int delete = userMapper.delete(wrapper5);
        return delete > 0 ? JsonResult.ok("已删除") : JsonResult.errorException("服务器错误");
    }

    @Override
    public JsonResult getUserInfo() {
        Integer userId = UserUtils.getUserId();
        User user1 = null;
        UserInfoDto userInfoDto = null;
        if (redisCache.getCacheObject("userId:" + userId) == null) {
            user1 = userMapper.selectById(userId);

            String className = null;
            try {
                className = gradeMapper.selectById(user1.getGradeId()).getClassName();
            } catch (NullPointerException e) {
                className = "未加入班级";
            }

            userInfoDto = new UserInfoDto(user1,className);
            redisCache.setCacheObject("userId:" + userId, userInfoDto);
        } else {
            userInfoDto = redisCache.getCacheObject("userId:" + userId);
        }
        return userInfoDto == null ? JsonResult.errorException("数据为空") : JsonResult.ok(userInfoDto);
    }


    @Override
    public JsonResult sign(Integer signId, Integer courseId) {
        if (signId == null) {
            return JsonResult.errorException("数据不能为空");
        }

        if (!redisCache.getCacheList("courseId" + courseId).isEmpty()) {

            List<Integer> cacheList = redisCache.getCacheList("courseId" + courseId);
            Integer userId = UserUtils.getUserId();
            List<Integer> signed = new ArrayList<>();
            Integer remove = null;
            for (int i = 0; i < cacheList.size(); i++) {
                if (userId.equals(cacheList.get(i))) {
                    signed.add(cacheList.get(i));
                    remove = cacheList.remove(i);
                }
            }
            log.info(signed.toString());
            redisCache.setCacheList("signed" + courseId, signed);
            Sign sign = signMapper.selectById(signId);

            String signedIds = sign.getSignedIds();

            signedIds += userId+",";
            String unsignedIds = sign.getUnsignedIds();
            if(unsignedIds.contains(",,")) {
                unsignedIds = unsignedIds.replaceAll(",,", ",");
            }
            String s = unsignedIds.replaceAll(userId+",", "");
            log.info(s+"**********************");
            if (sign.getNoSignedNums()!=0) {
                signMapper.updateById(new Sign(signId, null, null, null, sign.getSignedNums() + 1, sign.getNoSignedNums() - 1, s, signedIds));
            }else {
                signMapper.updateById(new Sign(signId, null, null, null, sign.getSignedNums() + 1, 0, s, signedIds));
            }
            return remove > 0 ? JsonResult.ok("已签到") : JsonResult.errorException("服务器错误");
        } else {
            return JsonResult.errorException("签到时间已过");
        }
    }

    @Override
    public JsonResult updatePassword(String email, String code, String newPassword) {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(code) || StringUtils.isEmpty(newPassword)) {
            return JsonResult.errorException("数据不能为空");
        }
        if (StringUtils.isEmpty(redisCache.getCacheObject(email))) {
            return JsonResult.errorException("验证码已失效");
        }
        if (!code.equals(redisCache.getCacheObject(email).toString()) || !(redisCache.getCacheObject(email).toString()).equals(code)) {
            return JsonResult.errorException("验证码错误");
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        User user = new User();
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        int update = userMapper.update(user, wrapper);
        Integer userId = UserUtils.getUserId();
        redisCache.deleteObject("userId:" + userId);
        return update > 0 ? JsonResult.ok("已更新") : JsonResult.errorException("服务器异常");
    }

    @Override
    public JsonResult uploadFile(Integer courseId, String fileName, MultipartFile fileAddress, Date createTime) {
        if (courseId == null || StringUtils.isEmpty(fileName)) {
            return JsonResult.errorException("数据不能为空");
        }
        if (fileAddress.isEmpty()) {
            return JsonResult.errorException("上传文件不能为空");
        }
        String upload = cosUtil.upload(fileAddress, 1);
        Integer userId = UserUtils.getUserId();
        int insert = uploadFileMapper.insert(new UploadFile(null, userId, courseId, fileName, upload, null));
        return insert > 0 ? JsonResult.ok("已上传") : JsonResult.errorException("服务器错误");
    }

    @Override
    public JsonResult updateUserInfoById(UserInfoVo userInfoVo, Integer id) {
        if (userInfoVo == null || id == null) {
            return JsonResult.errorException("数据不能为空");
        }
        User user = new User(id, null, null, null, null, userInfoVo.getSex(), userInfoVo.getMajor(), userInfoVo.getGradeId(), userInfoVo.getNativePlace(), null, null, null, null);
        int update = userMapper.updateById(user);
        redisCache.deleteObject("userId:" + id);
        return update > 0 ? JsonResult.ok("已更新") : JsonResult.errorException("服务器异常");
    }

    @Override
    public JsonResult resetPassword(Integer id) {
        if (id == null) {
            return JsonResult.errorException("数据未输入");
        }
        User user = new User();
        user.setStudentId(id);
        user.setPassword(new BCryptPasswordEncoder().encode("123456"));
        int update = userMapper.updateById(user);
        return update > 0 ? JsonResult.ok() : JsonResult.errorException("服务器错误");
    }

    @Override
    public JsonResult allAddress() {
        Object address = redisCache.getCacheObject("address");
        Map<String, Object> map = new HashMap<>();
        map.put("address", address);
        return JsonResult.ok(map);
    }

    @Override
    public JsonResult sendMessage(String bizid, String message) {
        if (StringUtils.isEmpty(message)) {
            return JsonResult.errorException("数据不能为空");
        }
        try {
            WsServer.broadcast(bizid, message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return JsonResult.ok();
    }

    @Override
    public JsonResult getRandomName(Integer courseId) {
        if (courseId == null) {
            return JsonResult.errorException("数据不能为空");
        }
        Object cacheObject = redisCache.getCacheObject("randomName" + courseId);
        if ("".equals(cacheObject.toString())) {
            return JsonResult.errorException("无数据");
        }
        return JsonResult.ok(cacheObject);
    }

    @Override
    public JsonResult getTeacherInfo(Integer id) {
        if (id == null) {
            return JsonResult.errorException("数据不能为空");
        }
        User user = userMapper.selectById(id);
        return user == null ? JsonResult.errorException("无数据") : JsonResult.ok(user);
    }

    @Override
    public JsonResult groupInfo(Integer courseId) {
        if (courseId == null) {
            return JsonResult.errorException("数据不能为空");
        }
        Map<String, List> cacheObject = redisCache.getCacheObject("groups:" + courseId);
        return cacheObject == null ? JsonResult.errorException("无数据") : JsonResult.ok(cacheObject);
    }

    @Override
    public JsonResult sendMessage2(String content, String groupId, Integer courseId) {
        try {
            log.info(courseId + groupId + "*****************");
            WsServer.broadcast(courseId + groupId, content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Integer userId = UserUtils.getUserId();
        int insert = groupChatMapper.insert(new GroupChat(null, userId, courseId, groupId, content, null));
        return insert > 0 ? JsonResult.ok() : JsonResult.errorException("服务器错误");
    }

    @Override
    public JsonResult getMessageHistory(Integer courseId, String groupId, Integer nodePage, Integer pageSize) {
        if (courseId == null || groupId == null) {
            return JsonResult.errorException("数据不能为空");
        }
        if (nodePage <= 0 || pageSize <= 0) {
            return JsonResult.errorException("分页数据错误");
        }
        Page<GroupChat> page = new Page<>(nodePage, pageSize);
        LambdaQueryWrapper<GroupChat> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GroupChat::getGroupId, groupId).eq(GroupChat::getCourseId, courseId).orderByDesc(GroupChat::getCreateTime);
        Page<GroupChat> page1 = groupChatMapper.selectPage(page, wrapper);
        return page1.getSize() == 0 ? JsonResult.errorException("无数据") : JsonResult.ok(page1);
    }

    @Override
    public JsonResult addTerm(Date beginTime, Date endTime, String name) {
        if ("".equals(beginTime) || "".equals(endTime) || StringUtils.isEmpty(name)) {
            return JsonResult.errorException("数据错误");
        }
        int insert = termMapper.insert(new Term(null, beginTime, endTime, name));
        return insert > 0 ? JsonResult.ok() : JsonResult.errorException("服务器错误");

    }

    @Override
    public JsonResult getTerm() {
        List<Term> terms = termMapper.selectList(null);
        return terms.isEmpty() ? JsonResult.errorException("服务器错误") : JsonResult.ok(terms);
    }

    @Override
    public JsonResult whetherSign(Integer courseId) {
        if (courseId == null) {
            return JsonResult.errorException("数据不能为空");
        }
        List<Integer> cacheList = redisCache.getCacheList("signed" + courseId);
        Integer userId = UserUtils.getUserId();
        return cacheList.contains(userId) ? JsonResult.ok("已签到") : JsonResult.ok("未签到");
    }

    @Override
    public JsonResult deleteFile(Integer id) {
        if (id == null) {
            return JsonResult.errorException("数据不能为空");
        }
        String fileAddress = uploadFileMapper.selectById(id).getFileAddress();
        cosUtil.deleteObject(fileAddress, 1);
        int i = uploadFileMapper.deleteById(id);
        return i > 0 ? JsonResult.ok() : JsonResult.errorException("服务器错误");
    }

    @Override
    public JsonResult myGrouping(Integer courseId) {
        if (courseId == null) {
            return JsonResult.errorException("数据不能为空");
        }
        String username = UserUtils.getLoginUser().getUsername();
        Map<String, Set<User>> cacheObject = redisCache.getCacheObject("groups:" + courseId);
        for (Set<User> set : cacheObject.values()) {
            for (User user : set) {
                log.info(user.toString());
                if (username.equals(user.getUserName())) {
                    return JsonResult.ok(set);
                }
            }
        }
        return JsonResult.errorException("无数据");
    }

    @Override
    public JsonResult getGroupById(Integer courseId) {
        if (courseId == null) {
            return JsonResult.errorException("数据不能为空");
        }
        Map<String, Set<User>> map = redisCache.getCacheObject("groups:" + courseId);
        return map.isEmpty() ? JsonResult.errorException("无数据") : JsonResult.ok(map);
    }

    @Override
    public JsonResult updatePower(Integer id, Integer power) {
        if (id == null || power == null) {
            return JsonResult.errorException("数据不能为空");
        }
        User user = new User();
        user.setStudentId(id);
        user.setIdentity(power);
        int update = userMapper.updateById(user);
        return update > 0 ? JsonResult.ok() : JsonResult.errorException("服务器错误");
    }

    @Override
    public JsonResult getCourseSign(Integer courseId, Integer nodePage, Integer pageSize) {
        if (courseId == null) {
            return JsonResult.errorException("数据错误");
        }
        Page<Sign> page = new Page<>(nodePage, pageSize);
        LambdaQueryWrapper<Sign> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Sign::getCourseId,courseId);
        Page<Sign> page1 = signMapper.selectPage(page, wrapper);
        return page1.getSize() == 0 ? JsonResult.errorException("无数据") : JsonResult.ok(page1);
    }
}

