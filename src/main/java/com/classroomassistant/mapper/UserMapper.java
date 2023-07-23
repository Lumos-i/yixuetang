package com.classroomassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.classroomassistant.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zrq
 * @ClassName UserMapper
 * @date 2023/1/12 0:06
 * @Description TODO
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    List<User> grouping1(@Param("sex") String sex, @Param("courseId") Integer courseId);
    List<User> grouping2(@Param("sex") String sex, @Param("courseId") Integer courseId);


}
