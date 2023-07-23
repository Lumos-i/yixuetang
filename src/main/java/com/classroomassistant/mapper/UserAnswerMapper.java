package com.classroomassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.classroomassistant.pojo.User;
import com.classroomassistant.pojo.UserAnswer;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserAnswerMapper extends BaseMapper<UserAnswer> {

    List<User> getFinishHomework(Integer courseId);

}
